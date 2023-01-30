package memory

import chisel3._
import chisel3.util._
import chisel3.experimental.BaseModule
import chipsalliance.rocketchip.config.{Config, Field, Parameters}

case object RAMBlockSize extends Field[Int]
case object DataWidth extends Field[Int]
case object ReadBuffer extends Field[Int]

case object NumberOfNodes extends Field[Int]

class Indexed[+T <: Data](gen: T, w: Int) extends Bundle {
  val data = Output(gen)
  val idx = Output(UInt(w.W))
}

object Indexed {
  /** Wrap some [[Data]] in a indexed interface
   *  @tparam T the type of data to wrap
   *  @param gen the data to wrap
   *  @param w the bitwidth of the index
   *  @return the wrapped input data
   */
  def apply[T <: Data](gen: T, w: Int): Indexed[T] = new Indexed(gen, w)
}

trait RAMBankParams {
  implicit val p: Parameters
  val ramSize = p(RAMBlockSize)
  val dataWidth = p(DataWidth)
  val readBankSize = p(ReadBuffer)

  val addrWidth = log2Ceil(ramSize)
}

trait SharedRAMParams extends RAMBankParams {
  implicit val p: Parameters
  val nodes = p(NumberOfNodes)
  val idx_w = log2Ceil(nodes)
}

class BusReq(val dataWidth: Int, addrWidth: Int) extends Bundle {
  val addr = UInt(addrWidth.W)
  val wrena = Bool()
  val data = UInt(dataWidth.W)
}

class BusRes(val w: Int) extends Bundle {
  val data = UInt(w.W)
}

class BusSlaveBundle()(implicit val p: Parameters) extends Bundle
  with RAMBankParams {
  val req = Flipped(Decoupled(new BusReq(dataWidth, addrWidth)))
  val res = Decoupled(new BusRes(dataWidth))
}

class IndexedBusSlaveBundle()(implicit val p: Parameters) extends Bundle
  with RAMBankParams
  with SharedRAMParams {
  val req = Flipped(Decoupled(Indexed(new BusReq(dataWidth, addrWidth), idx_w)))
  val res = Decoupled(Indexed(new BusRes(dataWidth), idx_w))
}

/**
 * For write requests idx is DontCare
 * For read requests req.data is DontCare
 */
class RAMBankIndexed()(implicit val p: Parameters) extends Module
  with RAMBankParams
  with SharedRAMParams {
  val io = IO(new IndexedBusSlaveBundle()(p))

  val (read, write) = {
    val mem = SyncReadMem(ramSize, UInt(dataWidth.W))
    def read(addr: UInt, ren: Bool): Data = mem.read(addr, ren)
    def write(addr: UInt, wdata: UInt) = mem.write(addr, wdata)
    (read _, write _)
  }

  val ren = io.req.fire && !io.req.bits.data.wrena
  val wen = io.req.fire && io.req.bits.data.wrena

  val addr = io.req.bits.data.addr
  val curr_idx = io.req.bits.idx
  val rdata = read(addr, ren)

  when (wen) {
    write(addr, io.req.bits.data.data)
  }

  val q = Module(new Queue(Indexed(new BusRes(dataWidth), idx_w), readBankSize))
  q.io.enq.valid := RegNext(ren)
  q.io.enq.bits.data.data := rdata
  q.io.enq.bits.idx := curr_idx

  io.res <> q.io.deq

  io.req.ready := q.io.enq.ready
}
