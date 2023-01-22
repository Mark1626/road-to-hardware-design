package fact

import chisel3._
import chisel3.util._

class Fact extends Module {
  val io = IO(new Bundle {
    val in  = Flipped(Decoupled(UInt(16.W)))
    val out = Valid(UInt(16.W))
  })

  val busy = RegInit(false.B)
  val n    = RegInit(0.U(16.W))
  val acc  = RegInit(1.U(16.W))

  io.in.ready := !busy

  when (io.in.valid && !busy) {
    n     := io.in.bits
    acc   := 1.U(16.W)
    busy  := true.B
  }

  when (busy) {
    acc := acc * n
    n   := n - 1.U
  }

  io.out.bits   := acc
  io.out.valid  := n === 1.U && busy
  when (io.out.valid) {
    busy := false.B
  }
}
