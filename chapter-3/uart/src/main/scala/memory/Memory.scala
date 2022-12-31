package memory

import chisel3._

class Memory(
              val size: Int = 1024,
              val addrWidth: Int = 16,
              val dataWidth: Int = 8) extends Module {

  // Add check to prevent going out of bound
  val io = IO(new Bundle {
    val rdAddr = Input(UInt(addrWidth.W))
    val rdData = Output(UInt(dataWidth.W))
    val wrAddr = Input(UInt(addrWidth.W))
    val wrData = Input(UInt(addrWidth.W))
    val wrEna  = Input(Bool())
  })

  val mem = SyncReadMem(size, UInt(dataWidth.W))

  io.rdData := mem.read(io.rdAddr)

  when(io.wrEna) {
    mem.write(io.wrAddr, io.wrData)
  }
}
