package transform

import chisel3._

object TransformVerilog extends App {
  emitVerilog(new TransformTest(64), Array.concat(args, Array("-td=./verilog-output")))
}

class TransformTest(width: Int) extends Module {
  val io = IO(new Bundle {
    val in  = Input(Vec(width, UInt(16.W)))
    val out  = Output(Vec(width, UInt(16.W)))
    val median = Output(UInt(16.W))
  })

  val med = RegInit(UInt(16.W), 0.U)
  val mid = width/2

  med := Mux((width & 1).U === 1.U, io.in(mid), (io.in(mid) + io.in(mid-1)) >> 1)

  for (i <- 0 until width/2) {
    io.out(i) := RegNext(med - io.in(i))
  }

  for (i <- width/2 until width) {
    io.out(i) := RegNext(io.in(i) - med)
  }

  io.median := med

}
