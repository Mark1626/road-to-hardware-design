import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._
import scala.util.Random

class TestHarnessSpec extends AnyFreeSpec with ChiselScalatestTester {
  "should be able to function" in {
    test(new TopModule())
      .withAnnotations(Seq(WriteVcdAnnotation, VerilatorBackendAnnotation)) { dut =>
        dut.clock.step(500)
      }
  }
}