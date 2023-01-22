package fact

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._
import scala.util.Random

class FactSpec extends AnyFreeSpec with ChiselScalatestTester {
  "should be able to calculate fact" in {
    test(new Fact)
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
        val n = 5

        dut.clock.step()
        dut.io.in.bits.poke(n.U)
        dut.io.in.valid.poke(true.B)

        dut.clock.step()
        dut.io.in.valid.poke(false.B)

        for (i <- 0 until (n-1)) {
          dut.clock.step()
        }

        dut.io.out.valid.expect(true.B)
        dut.io.out.bits.expect(120.U)
    }
  }
}
