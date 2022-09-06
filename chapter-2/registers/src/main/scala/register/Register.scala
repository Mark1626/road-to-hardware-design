package register

import chisel3._

object RegisterVerilog extends App {
  emitVerilog(new RegisterTest(), Array.concat(args, Array("-td=./verilog-output")))
}

class RegisterTest extends Module {
  val io = IO(new Bundle {
    val in  = Input(UInt(16.W))
    val out  = Output(UInt(16.W))
  })

  val stage0 = RegNext(io.in)
  val stage1 = RegNext(stage0)
  val stage2 = RegNext(stage1)
  val stage3 = RegNext(stage2)

  io.out := stage3
}
