package fp

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._
import chisel3.experimental.FixedPoint._
import scala.util.Random

class TransformTestSpec extends AnyFreeSpec with ChiselScalatestTester {
  "Should be able to transform values" in {
    val width = 4

    test(new TransformTest(width)) { c =>

      // val fp = FixedPoint.fromDouble(3.8, 8.W, 0.BP)
      val f1bp5 = 1.5.F(1.BP)
      val f1 = f1bp5 << 2

      assert(f1 == 6.0.F)

      // .withAnnotations(Seq(WriteVcdAnnotation)) { c =>

        // for (iter <- 0 until 100) {
        //   val dataSeq = Seq.tabulate(width)(_ => Random.nextInt(32768)).sorted
        //   // val dataSeq = Seq.tabulate(width)(i => i).sorted

        //   for (i <- 0 until width) {
        //     c.io.in(i).poke(dataSeq(i))
        //   }

        //   c.clock.step()
        //   c.clock.step()

        //   val med = (dataSeq(width/2) + dataSeq(width/2 - 1)) >> 1

        //   for (i <- 0 until width) {
        //     c.io.out(i).expect(Math.abs(dataSeq(i) - med).U)
        //   }
        // }

      }
  }
}
