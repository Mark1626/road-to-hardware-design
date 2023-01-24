package modules

import chipsalliance.rocketchip.config.{Config, Parameters}
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class ThresholdDetectorSpec extends AnyFreeSpec with ChiselScalatestTester with Matchers {
  val p: Parameters = new Config((site, here, up) => {
    case FixedPointWidth => 8
    case BinaryPointWidth => 4
  })

  "should be able to return true when value above threshold" in {
    test(new ThresholdDetectorChiselModule()(p))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.io.threshold.poke(3.0.F(8.W, 4.BP))
      dut.io.in.bits.poke(5.0.F(8.W, 4.BP))
      dut.io.in.valid.poke(true.B)
      dut.clock.step()

      dut.io.in.valid.poke(false.B)
      dut.clock.step()

      dut.io.out.ready.poke(true.B)
      dut.clock.step()

      dut.io.out.valid.expect(true.B)
      dut.io.out.bits.expect(true.B)
      dut.clock.step()
    }
  }

  "should be able to return false when value above threshold" in {
    test(new ThresholdDetectorChiselModule()(p))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
        dut.io.threshold.poke(3.0.F(8.W, 4.BP))
        dut.io.in.bits.poke(1.5.F(8.W, 4.BP))
        dut.io.in.valid.poke(true.B)
        dut.clock.step()

        dut.io.in.valid.poke(false.B)
        dut.clock.step()

        dut.io.out.ready.poke(true.B)
        dut.clock.step()

        dut.io.out.valid.expect(true.B)
        dut.io.out.bits.expect(false.B)
        dut.clock.step()
      }
  }
}
