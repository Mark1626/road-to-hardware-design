package memory

import chipsalliance.rocketchip.config.{Config, Parameters}
import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class MemArbiterSpec extends AnyFreeSpec with ChiselScalatestTester {
  val n = 3
  val p: Parameters = new Config((site, here, up) => {
    case RAMBlockSize => 1024
    case DataWidth => 16
    case ReadBuffer => 8
    case ArbQueueDepth => 4
    case BusWidth => 32
    case NumberOfNodes => n
  })

  class Helper(dut: MemArbiter) {
    def enqueueReadReq(addr: Int, idx: Int): Unit = {
      while (dut.io.req_in.ready.peek() == false.B) { dut.clock.step() }
      dut.io.req_in.bits.data.addr.poke(addr)
      dut.io.req_in.bits.data.wrena.poke(false.B)
      dut.io.req_in.bits.idx.poke(idx.U)
      dut.io.req_in.valid.poke(true.B)
      dut.clock.step()

      dut.io.req_in.valid.poke(false.B)
      dut.clock.step()
    }

    def currentReq(): (BusReq, UInt) = {
      (dut.io.req_out.bits.data, dut.io.req_out.bits.idx)
    }

    def dequeueReadReq() = {
      dut.io.req_out.ready.poke(true.B)
      dut.clock.step()
      while(dut.io.req_out.valid.peek() == false.B) { dut.clock.step() }
    }
  }

  "Memory arbiter waveform analysis" in {
    test(new MemArbiter(n)(p))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      val helper = new Helper(dut)
      helper.enqueueReadReq(100, 1)
      helper.enqueueReadReq(101, 2)
      helper.enqueueReadReq(102, 3)

      val res = helper.currentReq()
      res._2.expect(1)
      res._1.wrena.expect(false.B)
      res._1.addr.expect(100)

      helper.dequeueReadReq()
      res._2.expect(2)
      res._1.wrena.expect(false.B)
      res._1.addr.expect(101)
    }
  }
}
