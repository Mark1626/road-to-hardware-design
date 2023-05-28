package thruwire

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._
import scala.util.Random


class MACSpec extends AnyFreeSpec with ChiselScalatestTester {
  "Should be able to multiply accumulate" in {
    test(new MAC(4)) { t =>

      for (i <- 1 until 100) {
        val in_a = Random.nextInt(16)
        val in_b = Random.nextInt(16)
        val in_c = Random.nextInt(16)

        t.io.in_a.poke(in_a.U)
        t.io.in_b.poke(in_b.U)
        t.io.in_c.poke(in_c.U)
        t.io.out.expect((in_a * in_b + in_c).U)
      }
    }
  }
}
