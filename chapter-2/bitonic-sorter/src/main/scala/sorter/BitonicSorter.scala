package bitonic

import chisel3._
import chisel3.util._
import chisel3.experimental._

object Compartor {
  def switch[T <: FixedPoint](
      in1: T,
      in2: T,
      out1: T,
      out2: T,
      asc: Boolean
  ) = {
    // val exchg = if (asc) in1 < in2 else in1 > in2
    if (asc) {
      out1 := Mux(in1 < in2, in1, in2)
      out2 := Mux(in1 < in2, in2, in1)
    } else {
      out1 := Mux(in1 > in2, in1, in2)
      out2 := Mux(in1 > in2, in2, in1)
    }
    
  }
}

class BitonicSortingStage[T <: FixedPoint](lines: Int, width: T, stage: Int)
    extends Module {
  val io = IO(new Bundle {
    var in = Input(Vec(lines, width))
    var out = Output(Vec(lines, width))
  })

  var curr = Wire(Vec(lines, width))

  for (line <- 0 until lines) {
    curr(line) := io.in(line)
  }

  for (level <- stage - 1 to 0 by -1) {
    val stride = 1 << level
    var next = Reg(Vec(lines, width))

    Seq
      .tabulate(lines)(i => (i, i ^ stride))
      .filter({ case (a, b) => b > a })
      .map({ case (a, b) =>
        Compartor.switch(
          curr(a),
          curr(b),
          next(a),
          next(b),
          (a & (1 << stage)) == 0
        )
      })

    curr = next
  }

  for (line <- 0 until lines) {
    io.out(line) := curr(line)
  }
}

// This module hasn't been verified
class BitonicSorter[T <: FixedPoint](lines: Int, width: T) extends Module {

  val io = IO(new Bundle {
    var in = Input(Vec(lines, width))
    var out = Output(Vec(lines, width))
  })

  // Connecting this
  val stages = log2Ceil(lines)
  val networks = Seq.range(0, stages).map(stage =>
    Module(new BitonicSortingStage(lines, width, stage + 1))
  )

  networks(0).io.in <> io.in

  for (stage <- 1 until stages-1) {
    networks(stage).io.in <> networks(stage+1).io.out
  }

  io.out <> networks(stages-1).io.out
}
