import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config.{Config, Field, Parameters}

class PassthroughStorage()(implicit val p: Parameters) extends Module with BusParams {
  val io = IO(new WishboneSlave(busWidth))

  val ack = RegInit(false.B)
  val data_rd = RegInit(0.U(busWidth.W))

  io.bus.ack := ack
  io.bus.stall := DontCare
  io.bus.err := DontCare

  io.bus.data_rd := data_rd

  ack := false.B
  when (io.bus.stb && io.bus.cyc && !io.bus.ack) {
    data_rd := io.bus.addr
    ack := true.B
  }
}