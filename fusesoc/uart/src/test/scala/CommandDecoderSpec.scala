import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

import scala.util.Random

class CommandDecoderSpec extends AnyFlatSpec with ChiselScalatestTester with Matchers {
  it should "should be able to decode an incoming command" in {
    test(new CommandDecoder())
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
        dut.io.in.initSource()
        dut.io.in.setSourceClock(dut.clock)
        dut.io.out.initSink()
        dut.io.out.setSinkClock(dut.clock)

        for (_ <- 1 until 100) {
          val input = Seq.tabulate(4)(_ => Random.nextInt(256).toLong)

          var expected: Long = 0L
          expected += input(3)
          expected += input(2) << 8
          expected += input(1) << 16
          expected += input(0) << 24

          dut.io.in.enqueueSeq(input.map(d => d.U))
          dut.io.out.expectDequeue(expected.U)
        }
      }
  }
}
