package bitonic

import chisel3._

class Compartor(width: Int, ascending: Boolean) extends Module {
  val io = IO(new Bundle {
    val in0 = Input(UInt(width.W))
    val in1 = Input(UInt(width.W))
    val out0 = Output(UInt(width.W))
    val out1 = Output(UInt(width.W))
  })

  def comp(l: UInt, r: UInt): Bool = {
    if (ascending)
      l < r
    else
      l > r
  }

  when (comp(io.in0, io.in1)) {
    io.out0 := io.in0
    io.out1 := io.in1
  } otherwise {
    io.out0 := io.in1
    io.out1 := io.in0
  }
}

class BitonicSorter(width: Int) extends Module {
  val io = IO(new Bundle {
    val in0 = Input(UInt(width.W))
    val in1 = Input(UInt(width.W))
    val in2 = Input(UInt(width.W))
    val in3 = Input(UInt(width.W))
    val out0 = Output(UInt(width.W))
    val out1 = Output(UInt(width.W))
    val out2 = Output(UInt(width.W))
    val out3 = Output(UInt(width.W))
  })

  val row00 = Module(new Compartor(width, true))
  val row01 = Module(new Compartor(width, false))

  row00.io.in0 := RegNext(io.in0)
  row00.io.in1 := RegNext(io.in1)

  row01.io.in0 := RegNext(io.in2)
  row01.io.in1 := RegNext(io.in3)

  val row10 = Module(new Compartor(width, true))
  val row11 = Module(new Compartor(width, true))

  row10.io.in0 := RegNext(row00.io.out0)
  row10.io.in1 := RegNext(row01.io.out0)

  row11.io.in0 := RegNext(row00.io.out1)
  row11.io.in1 := RegNext(row01.io.out1)

  val row20 = Module(new Compartor(width, true))
  val row21 = Module(new Compartor(width, true))

  row20.io.in0 := RegNext(row10.io.out0)
  row20.io.in1 := RegNext(row11.io.out0)

  row21.io.in0 := RegNext(row10.io.out1)
  row21.io.in1 := RegNext(row11.io.out1)

  // Read output

  io.out0 := row20.io.out0
  io.out1 := row20.io.out1
  io.out2 := row21.io.out0
  io.out3 := row21.io.out1
}
