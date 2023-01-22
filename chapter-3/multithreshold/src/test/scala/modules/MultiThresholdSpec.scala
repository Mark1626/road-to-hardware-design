package modules

import chipsalliance.rocketchip.config.{Config, Parameters}
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class MultiThresholdWishboneSpec(dut: WishboneThresholdDetector) {
  def wishboneWrite(addr: Long, data: Long): Unit = {
    dut.io.bus.addr.poke(addr)
    dut.io.bus.data_wr.poke(data)
    dut.io.bus.we.poke(true)
    dut.io.bus.cyc.poke(true)
    dut.io.bus.stb.poke(true)
    while (dut.io.bus.ack.peek() != true.B) dut.clock.step(1)
    dut.io.bus.cyc.poke(false)
    dut.io.bus.stb.poke(false)
    dut.clock.step(1)
    dut.io.bus.ack.expect(0)
  }

  def wishboneRead(addr: Long): Unit = {
    dut.io.bus.addr.poke(addr)
    dut.io.bus.we.poke(false)
    dut.io.bus.cyc.poke(true)
    dut.io.bus.stb.poke(true)
    while (dut.io.bus.ack.peek() != true.B) dut.clock.step(1)
    dut.io.bus.cyc.poke(false)
    dut.io.bus.stb.poke(false)
    val data_out = dut.io.bus.data_rd.peek()
    dut.clock.step(1)
    dut.io.bus.ack.expect(0)
    return data_out
  }
}

class MultiThresholdSpec extends AnyFreeSpec with ChiselScalatestTester with Matchers {
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
