import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config.{Config, Field, Parameters}

class Reader(val offset: Int)(implicit val p: Parameters) extends Module with BusParams {
  val io = IO(new Bundle {
    val down = new WishboneMaster(busWidth)
    val addr = Flipped(Decoupled(UInt(busWidth.W)))
    val data = Decoupled(UInt(busWidth.W))
  })

  val idle :: bus_req :: bus_res :: Nil = Enum(3)
  val state = RegInit(idle)

  val activeTrn = RegInit(false.B)
  val addr = RegInit(1.U(busWidth.W))
  val data_rd = RegInit(0.U(busWidth.W))

  io.addr.ready := state === idle

  io.data.valid := false.B
  io.data.bits := DontCare

  io.down.bus.stb := activeTrn
  io.down.bus.cyc := activeTrn
  io.down.bus.sel := DontCare
  io.down.bus.we := false.B
  io.down.bus.data_wr := DontCare
  io.down.bus.addr := addr


  when (state === idle && io.addr.fire) {
    io.down.bus.stb := true.B
    io.down.bus.cyc := true.B
    io.down.bus.addr := offset.U + io.addr.bits

    addr := offset.U + io.addr.bits
    activeTrn := true.B
    state := bus_req
  } .elsewhen (state === bus_req && io.down.bus.ack === true.B) {
    data_rd := io.down.bus.data_rd
    state := bus_res
    activeTrn := false.B
  } .elsewhen (state === bus_res && io.data.ready) {
    io.data.valid := true.B
    io.data.bits := data_rd
    state := idle
  }
}
