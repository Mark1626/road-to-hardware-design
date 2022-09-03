package thruwire

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._
import scala.util.Random

class ArbiterSpec extends AnyFreeSpec with ChiselScalatestTester {
  "Should be able to multiply accumulate" in {
    test(new Arbiter) { c =>
      val data = Random.nextInt(32768)
      c.io.fifo_data.poke(data.U)

      for (i <- 0 until 8) {
        c.io.fifo_valid.poke((((i >> 0) % 2) != 0).B)
        c.io.pe0_ready.poke((((i >> 1) % 2) != 0).B)
        c.io.pe1_ready.poke((((i >> 2) % 2) != 0).B)

        c.io.fifo_ready.expect((i > 1).B)
        c.io.pe0_valid.expect((i == 3 || i == 7).B)
        c.io.pe1_valid.expect((i == 5).B)

        if (i == 3 || i == 7) {
          c.io.pe0_data.expect((data).U)
        } else if (i == 5) {
          c.io.pe1_data.expect((data).U)
        }
      }
    }
  }
}
