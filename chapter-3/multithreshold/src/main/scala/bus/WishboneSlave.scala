package bus

import chisel3._
class WishboneSlave(val N: Int) extends Bundle {
  assert(N % 8 == 0)
  val NUM_BYTES = N / 8

  val cyc = Input(Bool())
  val stb = Input(Bool())
  val we = Input(Bool())
  val sel = Input(UInt(NUM_BYTES.W))
  val addr = Input(UInt(N.W))
  val data_wr = Input(UInt(N.W))

  val ack = Output(Bool())
  val err = Output(Bool())
  val data_rd = Output(UInt(N.W))
}