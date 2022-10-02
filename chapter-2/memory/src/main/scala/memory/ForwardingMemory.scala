package memory

import chisel3._


class ForwardingMemory extends Module {
  val io = IO(new Bundle {
    val rdAddr = Input(UInt(16.W))
    val rdData = Output(UInt(8.W))
    val wrAddr = Input(UInt(16.W))
    val wrData = Input(UInt(8.W))
    val wrEna  = Input(Bool())
  })

  val mem = SyncReadMem(1024, UInt(8.W))

  val wrAddrReg = RegNext(io.wrData)
  val doForwardReg = RegNext((io.wrAddr === io.rdAddr) && io.wrEna)

  val memData = mem.read(io.rdAddr)

  when (io.wrEna) {
    mem.write(io.wrAddr, io.wrData)
  }

  io.rdData := Mux(doForwardReg, wrAddrReg, memData)
}
