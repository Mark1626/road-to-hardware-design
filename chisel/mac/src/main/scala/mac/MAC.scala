package thruwire

import chisel3._

class MAC(width: Int) extends Module {
  val io = IO(new Bundle {
    val in_a = Input(UInt(width.W))
    val in_b = Input(UInt(width.W))
    val in_c = Input(UInt(width.W))
    val out = Output(UInt((2 * width).W))
  })
  io.out := io.in_a * io.in_b + io.in_c
}
