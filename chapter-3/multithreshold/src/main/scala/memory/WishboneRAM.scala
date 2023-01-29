package memory

import bus.WishboneSlave
import chisel3._
import chipsalliance.rocketchip.config.Parameters

// Note: I'm not 100% the read is working correctly
class WishboneRAM(val busWidth: Int = 32)(implicit val p: Parameters) extends Module
  with RAMBankParams {
  val io = IO(new Bundle {
    val bus = new WishboneSlave(busWidth)
  })

  val (read, write) = {
    val mem = SyncReadMem(ramSize, UInt(dataWidth.W))
    def read(addr: UInt, ren: Bool): Data = mem.read(addr, ren)
    def write(addr: UInt, wdata: UInt): Unit = mem.write(addr, wdata)
    (read _, write _)
  }

  private val ack = RegInit(false.B)

  io.bus.err := false.B
  io.bus.sel := DontCare
  io.bus.stall := false.B

  io.bus.ack := ack

  private val ren = RegInit(false.B)

  io.bus.data_rd := read(io.bus.addr, ren)

  ack := false.B
  when (io.bus.stb && io.bus.cyc && !io.bus.ack) {
    // Read Request
    ren := !io.bus.we

    // Write Request
    when(io.bus.we) {
      write(io.bus.addr, io.bus.data_wr)
    }

    ack := true.B
  }
}
