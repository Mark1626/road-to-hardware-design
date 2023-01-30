package modules

import chipsalliance.rocketchip.config.Parameters
import chisel3._
import chisel3.util._
import memory.{RAMBankIO, RAMBankIndexed, RAMBankParams, SharedRAMParams}

class TopIO() extends Bundle {
  val addr = Flipped(Decoupled(UInt(32.W)))
  val data_rd = Decoupled(UInt(32.W))
}

class ReaderIO()(implicit val p: Parameters) extends Bundle {
  val mem = Flipped(new RAMBankIO()(p))
  val top = new TopIO()
}

class Reader()(implicit val p: Parameters) extends Module
  with SharedRAMParams {
  val io = IO(new ReaderIO())

  val idle :: read_req_start :: read_req_done :: done :: Nil = Enum(4)
  val state = RegInit(idle)

  val data_valid = RegInit(false.B)
  val data = Reg(UInt(32.W))

  io.mem.req.valid := false.B
  // Read Request, data is DontCare, wrena is false
  io.mem.req.bits.data.data := DontCare
  io.mem.req.bits.data.wrena := false.B
  io.mem.req.bits.data.addr := io.top.addr.bits
  io.mem.req.bits.idx := 1.U

  io.mem.res.ready := false.B

  io.top.addr.ready := state === idle

  io.top.data_rd.bits := data
  io.top.data_rd.valid := false.B

  when (state === idle && io.top.addr.valid) {
    state := read_req_start
  } .elsewhen(state === read_req_start) {
    io.mem.req.valid := true.B
    state := read_req_done
  } .elsewhen (state === read_req_done) {
    io.mem.res.ready := true.B
    when(io.mem.res.valid) {
      data := io.mem.res.bits.data.data
      state := done
    }
  } .elsewhen (state === done) {
    io.top.data_rd.valid := true.B
    state := idle
  }
}

class RAMCoupledBlock()(implicit val p: Parameters) extends Module
  with SharedRAMParams {
  val io = IO(new Bundle{
    val top = new TopIO()
  })

  val ram = Module(new RAMBankIndexed()(p))
  val node = Module(new Reader()(p))

  io.top.addr <> node.io.top.addr

  node.io.mem.req <> ram.io.req

  ram.io.res <> node.io.mem.res

  node.io.top.data_rd <> io.top.data_rd

}
