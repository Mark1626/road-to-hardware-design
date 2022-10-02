package memory

import chisel3._

object RegisterVerilog extends App {
  emitVerilog(new Memory(), Array.concat(args, Array("-td=./verilog-output")))
}

class Memory extends Module {
  val io = IO(new Bundle {
    val rdAddr = Input(UInt(16.W))
    val rdData = Output(UInt(8.W))
    val wrAddr = Input(UInt(16.W))
    val wrData = Input(UInt(8.W))
    val wrEna  = Input(Bool())
  })
  
  val mem = SyncReadMem(1024, UInt(8.W))

  io.rdData := mem.read(io.rdAddr)

  when(io.wrEna) {
    mem.write(io.wrAddr, io.wrData)
  }

}
