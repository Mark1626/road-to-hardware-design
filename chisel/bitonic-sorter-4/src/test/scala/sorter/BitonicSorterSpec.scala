package bitonic

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._
import scala.util.Random

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
