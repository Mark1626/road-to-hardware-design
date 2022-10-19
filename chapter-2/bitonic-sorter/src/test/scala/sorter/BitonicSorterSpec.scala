package bitonic

import chisel3._
import chisel3.experimental._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._
import scala.util.Random

class ComparatorSpec extends AnyFreeSpec with ChiselScalatestTester {
  "Compartor" - {
    "should be able to swap lines ascending" in {

      class CmpModule(asc: Boolean) extends Module {
        val io = IO(new Bundle {
          val in1 = Input(FixedPoint(8.W, 4.BP))
          val in2 = Input(FixedPoint(8.W, 4.BP))
          val out1 = Output(FixedPoint(8.W, 4.BP))
          val out2 = Output(FixedPoint(8.W, 4.BP))
        })
        Compartor.switch(io.in1, io.in2, io.out1, io.out2, asc)
      }

      test(new CmpModule(true)) { dut =>
        for (i <- 1 until 100) {
          val a = Random.nextFloat()
          val b = Random.nextFloat()

          val af = FixedPoint.fromDouble(a, 8.W, 4.BP)
          val bf = FixedPoint.fromDouble(b, 8.W, 4.BP)

          dut.io.in1.poke(af)
          dut.io.in2.poke(bf)

          if (a < b) {
            dut.io.out1.expect(af)
            dut.io.out2.expect(bf)
          } else {
            dut.io.out1.expect(bf)
            dut.io.out2.expect(af)
          }
        }
      }

      test(new CmpModule(false)) { dut =>
        for (i <- 1 until 100) {
          val a = Random.nextFloat()
          val b = Random.nextFloat()

          val af = FixedPoint.fromDouble(a, 8.W, 4.BP)
          val bf = FixedPoint.fromDouble(b, 8.W, 4.BP)

          dut.io.in1.poke(af)
          dut.io.in2.poke(bf)

          if (a > b) {
            dut.io.out1.expect(af)
            dut.io.out2.expect(bf)
          } else {
            dut.io.out1.expect(bf)
            dut.io.out2.expect(af)
          }
        }
      }
    }
  }
}

class BitonicSorterSpec extends AnyFreeSpec with ChiselScalatestTester {

  "BitonicSortingStage" - {
    "should be able to create a single level sorting stage" in {
      test(new BitonicSortingStage(4, FixedPoint(8.W, 4.BP), 1))
        .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
        for (iter <- 0 until 100) {
          val randSeq = Seq.tabulate(4)(_ => Random.nextFloat())
          val randSeqFP = randSeq.map(f => FixedPoint.fromDouble(f, 8.W, 4.BP))
          val (a, b) = randSeq.splitAt(2)
          val golden = a.sorted ++ b.sorted.reverse

          for (i <- 0 until 4)
            dut.io.in(i).poke(randSeqFP(i))

          for (i <- 0 until 4)
            dut.clock.step()

          for (i <- 0 until 4)
            dut.io.out(i).expect(golden(i).F(4.BP))
        }
      }
    }

    "should be able to create a multi level sorting stage" in {
      test(new BitonicSortingStage(4, FixedPoint(8.W, 4.BP), 2))
        .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
          for (iter <- 0 until 100) {
            val randSeq = Seq.tabulate(4)(_ => Random.nextFloat())
            val (a, b) = randSeq.splitAt(2)
            val randSeqPar = a.sorted ++ b.sorted.reverse

            val randSeqFP = randSeqPar.map(f => FixedPoint.fromDouble(f, 8.W, 4.BP))
            val golden = randSeq.sorted

            for (i <- 0 until 4)
              dut.io.in(i).poke(randSeqFP(i))

            for (i <- 0 until 8)
              dut.clock.step()

            for (i <- 0 until 4)
              dut.io.out(i).expect(golden(i).F(4.BP))
          }
      }
    }
  }

}

class BitonicSorterSpec extends AnyFreeSpec with ChiselScalatestTester {
  "Should be sort input network lines" in {
    test(new BitonicSorter(4))
    .withAnnotations(Seq(WriteVcdAnnotation)) { t =>
      for (i <- 1 until 100) {
        val randSeq = Seq.range(0, 4).map(_ => Random.nextInt(16))
        val golden = randSeq.sorted

        t.io.in0.poke(randSeq(0).U)
        t.io.in1.poke(randSeq(1).U)
        t.io.in2.poke(randSeq(2).U)
        t.io.in3.poke(randSeq(3).U)

        t.clock.step()
        t.clock.step()
        t.clock.step()
        t.clock.step()

        t.io.out0.expect((golden(0)).U)
        t.io.out1.expect((golden(1)).U)
        t.io.out2.expect((golden(2)).U)
        t.io.out3.expect((golden(3)).U)
      }
    }
  }
}