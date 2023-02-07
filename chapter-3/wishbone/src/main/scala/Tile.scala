import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config.{Config, Field, Parameters}

class Tile(val n: Int)(implicit val p: Parameters) extends Module with BusParams {
  val io = IO(new Bundle {
    val addr: DecoupledIO[UInt] = Flipped(Decoupled(UInt(busWidth.W)))
    val res: DecoupledIO[UInt] = Decoupled(UInt(busWidth.W))
  })

  val reqReady = Wire(Vec(n, Bool()))

  val resData = RegInit(0.U(32.W))
  val resDone = Reg(Vec(n, Bool()))
  val done = RegInit(false.B)

  val readers = Seq.range(0, n).map(i => Module(new Reader(100 * i)(p)))
  val storage = Module(new PassthroughStorage()(p))

  when (io.addr.valid) {
    for (i <- 0 until n) {
      resDone(i) := false.B
      done := false.B
    }
  }

  // Upsteam Input
  for (i <- 0 until n) {
    readers(i).io.addr.bits <> io.addr.bits
    readers(i).io.addr.valid := io.addr.valid
    reqReady(i) := readers(i).io.addr.ready
  }

  io.addr.ready := !reqReady.contains(false.B)

  // Bus Arbiter Interconnect
  val arbiter = Module(new RRArbiter(Bool(), n))

  // TODO: Do I fire rty when arbiter.io.in.ready is false
  for (i <- 0 until n) {
    arbiter.io.in(i).bits := readers(i).io.down.bus.cyc
    arbiter.io.in(i).valid := readers(i).io.down.bus.cyc
  }
  arbiter.io.out.ready := false.B

  for (i <- 0 until n) {
    readers(i).io.down.bus.data_rd := DontCare
    readers(i).io.down.bus.err := DontCare
    readers(i).io.down.bus.stall := DontCare
    readers(i).io.down.bus.ack := false.B
  }

  storage.io.bus.stb := false.B
  storage.io.bus.cyc := false.B
  storage.io.bus.we := DontCare
  storage.io.bus.sel := DontCare
  storage.io.bus.addr := DontCare
  storage.io.bus.data_wr := DontCare

  when (arbiter.io.out.valid) {
    for (i <- 0 until n) {
      when (arbiter.io.chosen === i.U) {
        storage.io.bus.stb := readers(i).io.down.bus.stb
        storage.io.bus.cyc := readers(i).io.down.bus.cyc
        storage.io.bus.we := readers(i).io.down.bus.we
        storage.io.bus.sel := readers(i).io.down.bus.sel
        storage.io.bus.addr := readers(i).io.down.bus.addr
        storage.io.bus.data_wr := readers(i).io.down.bus.data_wr

        readers(i).io.down.bus.data_rd := storage.io.bus.data_rd
        readers(i).io.down.bus.err := storage.io.bus.err
        readers(i).io.down.bus.stall := storage.io.bus.stall
        readers(i).io.down.bus.ack := storage.io.bus.ack
      }
    }

    val reqEnd = RegNext(storage.io.bus.ack)
    // TODO: Verify this
    when (reqEnd) {
      storage.io.bus.stb := false.B
      storage.io.bus.cyc := false.B
    }

    // TODO: I'm partly skeptical about this, maybe storage.io.bus.cyc && storage.io.bus.ack
    arbiter.io.out.ready := reqEnd
//    arbiter.io.out.ready := RegNext(storage.io.bus.cyc)
  }

  // Upstream Output
  for (i <- 0 until n) {
    readers(i).io.data.ready := io.res.ready

    when(readers(i).io.data.valid) {
      resData := resData + readers(i).io.data.bits
      resDone(i) := true.B
    }
  }

  io.res.bits := resData
  io.res.valid := !resDone.contains(false.B)
}
