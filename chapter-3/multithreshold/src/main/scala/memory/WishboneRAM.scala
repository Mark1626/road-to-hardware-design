package memory

import bus.WishboneSlave
import chisel3._
import chipsalliance.rocketchip.config.Parameters

// Note: I'm not 100% the read is working correctly
class WishboneRAM()(implicit val p: Parameters) extends Module
  with BusParams
  with RAMBankParams {
  val io = IO(new WishboneSlave(busWidth))

  val (read, write) = {
    val mem = SyncReadMem(ramSize, UInt(busWidth.W))
    def read(addr: UInt, ren: Bool): Data = mem.read(addr, ren)
    def write(addr: UInt, wdata: UInt): Unit = mem.write(addr, wdata)
    (read _, write _)
  }

  private val ack = RegInit(false.B)
  private val data = RegInit(0.U(busWidth.W))

  io.bus.err := false.B
  io.bus.sel := DontCare
  io.bus.stall := false.B

  io.bus.ack := ack

  io.bus.data_rd := Mux(io.bus.we, data, read(io.bus.addr, !io.bus.we))

  ack := false.B
  data := 0.U
  when (io.bus.stb && io.bus.cyc && !io.bus.ack) {
    // Write Request
    when(io.bus.we) {
      write(io.bus.addr, io.bus.data_wr)
      data := io.bus.data_wr
    }

    ack := true.B
  }
}
