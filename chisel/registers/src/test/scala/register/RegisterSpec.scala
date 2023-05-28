package register

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._
import scala.util.Random

class RegisterTestSpec extends AnyFreeSpec with ChiselScalatestTester {
  "Should be able to shift values" in {
    test(new RegisterTest)
      .withAnnotations(Seq(WriteVcdAnnotation)) { c =>

        for (i <- 1 until 10) {
          c.clock.step()
          c.io.in.poke(i.U)
        }

      }
  }
}
