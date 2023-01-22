package memory

import chisel3._
import chisel3.util._
import chisel3.experimental.BaseModule
import chipsalliance.rocketchip.config.{Config, Field, Parameters}

case object RAMBlockSize extends Field[Int]
case object DataWidth extends Field[Int]

trait RAMBlockParams {
  implicit val p: Parameters
  val ramSize = p(RAMBlockSize)
  val dataWidth = p(DataWidth)

  def addrWidth: Int = log2Ceil(ramSize)
}

class RAMBlockIO(val addrWidth: Int, val dataWidth: Int) extends Bundle {
  val rdAddr  = Input(UInt(addrWidth.W))
  val rdData  = Output(UInt(dataWidth.W))
  val rdena   = Input(Bool())
  val wrAddr  = Input(UInt(addrWidth.W))
  val wrData  = Input(UInt(dataWidth.W))
  val wrena   = Input(Bool())
}

trait HasRAMMemIO extends BaseModule {
  val dataWidth: Int
  val addrWidth: Int

  val io = IO(new RAMBlockIO(addrWidth, dataWidth))
}

class RAMBlockChiselModule()(implicit val p: Parameters) extends Module
  with RAMBlockParams
  with HasRAMMemIO {
  val mem       = SyncReadMem(ramSize, UInt(dataWidth.W))
  val rdAddrReg = Reg(UInt())

  when (io.rdena) { rdAddrReg := io.rdAddr }
    .elsewhen(io.wrena) { mem.write(io.wrAddr, io.wrData) }

  io.rdData := mem.read(io.rdAddr)
}
