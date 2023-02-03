package modules

import chipsalliance.rocketchip.config.Parameters
import chisel3._
import chisel3.util._
import memory.{BusNodesParams, BusParams, BusReq, BusSlaveBundle, RAMBankIndexed}

class ModuleBundle()(implicit val p: Parameters) extends Bundle {
  val down = Flipped(new BusSlaveBundle()(p)) // Bus master
  val up = new BusSlaveBundle()(p)
}

class Reader()(implicit val p: Parameters) extends Module
  with BusParams {
  val io = IO(new ModuleBundle())

  val idle :: read_req_start :: read_req_done :: done :: Nil = Enum(4)
  val state = RegInit(idle)

  val data = Reg(UInt(32.W))

  // Downstream
  io.down.req.valid := false.B
  // Read Request, data is DontCare, wrena is false
  io.down.req.bits.data := DontCare
  io.down.req.bits.wrena := false.B
  io.down.req.bits.addr := io.up.req.bits.addr

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
      data := io.down.res.bits.data
      state := done
    }
  } .elsewhen (state === done) {
    io.up.res.valid := true.B
    state := idle
  }
}

class Writer()(implicit val p: Parameters) extends Module
  with BusParams {
  val io = IO(new ModuleBundle())

  val idle :: write_req_start :: write_req_done :: done :: Nil = Enum(4)
  val state = RegInit(false.B)

  // Downstream
  io.down.req.valid := false.B
  io.down.req.bits := DontCare

  io.down.res.ready := false.B

  // Upstream
  io.up.req.ready := state === idle
  io.up.res.bits.data := DontCare
  io.up.res.valid := false.B

  when (state === idle && io.up.req.valid) {
    io.down.req.bits.wrena := true.B
    io.down.req.bits.addr := io.up.req.bits.addr
    io.down.req.bits.data := io.up.req.bits.data
    io.down.req.valid := true.B

    io.up.res.bits.data := io.up.req.bits.data
    io.up.res.valid := true.B
  }
}

class RAMCoupledBlock()(implicit val p: Parameters) extends Module
  with BusNodesParams {
  val io = IO(new Bundle{
    val top = new BusSlaveBundle()
  })

  val ram = Module(new RAMBankIndexed()(p))
  val reader = Module(new Reader()(p))
  val writer = Module(new Writer()(p))

  val sel = io.top.req.bits.addr(19, 16)

  // Request: Top to Reader/Writer
  reader.io.up.req.valid := io.top.req.valid && (sel === 0.U)
  writer.io.up.req.valid := io.top.req.valid && (sel === 1.U)

  reader.io.up.req.bits.addr := io.top.req.bits.addr(15, 0)
  writer.io.up.req.bits.addr := io.top.req.bits.addr(15, 0)

  writer.io.up.req.bits.wrena := DontCare
  reader.io.up.req.bits.wrena := DontCare

  reader.io.up.req.bits.data := DontCare
  writer.io.up.req.bits.data := io.top.req.bits.data

  io.top.req.ready := Mux(sel === 0.U, reader.io.up.req.ready, writer.io.up.req.ready)

  // Response: Reader/Writer to Top
  reader.io.up.res.ready := io.top.res.ready && (sel === 0.U)
  writer.io.up.res.ready := io.top.res.ready && (sel === 1.U)

  reader.io.up.res.bits <> io.top.res.bits
  writer.io.up.res.bits <> io.top.res.bits

  io.top.res.valid := Mux(sel === 0.U, reader.io.up.res.valid, writer.io.up.res.valid)

  // Test
  ram.io.req.bits.data <> writer.io.down.req.bits
  ram.io.req.bits.idx <> 0.U
  writer.io.down.req.ready := ram.io.req.ready
  ram.io.req.valid := writer.io.down.req.valid

  writer.io.down.res.bits := ram.io.res.bits.data
  ram.io.res.ready := writer.io.down.res.ready
  writer.io.down.res.valid := ram.io.res.valid

  reader.io.down.req.ready := DontCare

  reader.io.down.res <> DontCare

  // Request: Reader/Writer to Mem

//  val arbiter = Module(new Arbiter(new BusReq(busWidth), 2))
//
//  arbiter.io.in(0) <> reader.io.down.req
//  arbiter.io.in(1) <> writer.io.down.req
//
//  arbiter.io.out.ready := ram.io.req.ready
//
//  ram.io.req.bits.data <> arbiter.io.out.bits
//  ram.io.req.bits.idx := arbiter.io.chosen
//
//  ram.io.req.valid := arbiter.io.out.valid
//
//  val res_sel = ram.io.res.bits.idx
//
//  reader.io.down.res.bits <> ram.io.res.bits.data
//  writer.io.down.res.bits <> ram.io.res.bits.data
//
//  ram.io.res.ready := Mux(res_sel === 0.U, reader.io.down.res.ready, writer.io.down.res.ready)
//
//  reader.io.down.res.valid := ram.io.res.valid && (res_sel === 0.U)
//  writer.io.down.res.valid := ram.io.res.valid && (res_sel === 1.U)
//
//  io.top.req <> reader.io.up.req
//
//  ram.io.req <> reader.io.down.req
//  ram.io.res <> reader.io.down.res
//
//  io.top.res <> reader.io.up.res
//
//  // Writer
//  writer.io.up.req <> io.top.req
//
//  writer.io.down.req <> ram.io.req
//  writer.io.down.res <> ram.io.res
//
//  writer.io.up.res <> io.top.res
}
