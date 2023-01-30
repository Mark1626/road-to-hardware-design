package modules

import chipsalliance.rocketchip.config.Parameters
import chisel3._
import chisel3.util._
import memory.{BusParams, BusSlaveBundle, IndexedBusSlaveBundle, RAMBankIndexed}

class ModuleBundle()(implicit val p: Parameters) extends Bundle {
  val down = Flipped(new IndexedBusSlaveBundle()(p)) // Bus master
  val up = new BusSlaveBundle()(p)
}

class Reader(val idx: Int)(implicit val p: Parameters) extends Module
  with BusParams {
  val io = IO(new ModuleBundle())

  val idle :: read_req_start :: read_req_done :: done :: Nil = Enum(4)
  val state = RegInit(idle)

  val data = Reg(UInt(32.W))

  // Downstream
  io.down.req.valid := false.B
  io.down.req.bits.idx := idx.U(idx_w.W)
  // Read Request, data is DontCare, wrena is false
  io.down.req.bits.data.data := DontCare
  io.down.req.bits.data.wrena := false.B
  io.down.req.bits.data.addr := io.up.req.bits.addr

  io.down.res.ready := false.B

  // Upstream
  io.up.req.ready := state === idle

  io.up.res.bits.data := data
  io.up.res.valid := false.B

  when (state === idle && io.up.req.valid) {
    state := read_req_start
  } .elsewhen(state === read_req_start) {
    io.down.req.valid := true.B
    state := read_req_done
  } .elsewhen (state === read_req_done) {
    io.down.res.ready := true.B
    when(io.down.res.valid) {
      data := io.down.res.bits.data.data
      state := done
    }
  } .elsewhen (state === done) {
    io.up.res.valid := true.B
    state := idle
  }
}

class Writer(val idx: Int)(implicit val p: Parameters) extends Module
  with BusParams {
  val io = IO(new ModuleBundle())

  val idle :: write_req_start :: write_req_done :: done :: Nil = Enum(4)
  val state = RegInit(false.B)

  // Downstream
  io.down.req.valid := false.B
  io.down.req.bits.idx := idx.U(idx_w.W)
  io.down.req.bits.data := DontCare

  io.down.res.ready := false.B

  // Upstream
  io.up.req.ready := state === idle
  io.up.res.bits.data := DontCare
  io.up.res.valid := false.B

  when (state === idle && io.up.req.valid) {
    io.down.req.bits.data.wrena := true.B
    io.down.req.bits.data.addr := io.up.req.bits.addr
    io.down.req.bits.data.data := io.up.req.bits.data
    io.down.req.valid := true.B

    io.up.res.bits.data := io.up.req.bits.data
    io.up.res.valid := true.B
  }
}

class RAMCoupledBlock()(implicit val p: Parameters) extends Module
  with BusParams {
  val io = IO(new Bundle{
    val top = new BusSlaveBundle()
  })

  val ram = Module(new RAMBankIndexed()(p))
  val reader = Module(new Reader(1)(p))
  val writer = Module(new Writer(2)(p))

  val read = io.top.req.bits.addr(19, 16) === 0x0.U

//  Mux(read, reader.io.up.req.valid, writer.io.up.req.valid) := io.top.req.valid

//  io.top.req <> Mux(read, reader.io.up.req, writer.io.up.req)
//
//  ram.io.req <> Mux(read, reader.io.down.req, writer.io.down.req)
//  ram.io.res <> Mux(read, reader.io.down.res, writer.io.down.res)
//
//  io.top.res <> Mux(read, reader.io.up.res, writer.io.down.res)

  io.top.req <> reader.io.up.req

  ram.io.req <> reader.io.down.req
  ram.io.res <> reader.io.down.res

  io.top.res <> reader.io.up.res

  // Writer
  writer.io.up.req <> io.top.req

  writer.io.down.req <> ram.io.req
  writer.io.down.res <> ram.io.res

  writer.io.up.res <> io.top.res
}
