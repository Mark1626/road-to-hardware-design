package modules

import chipsalliance.rocketchip.config.{Config, Parameters}
import chisel3._
import chisel3.util._
import chiseltest._
import memory.{ArbQueueDepth, DataWidth, NumberOfNodes, RAMBankSpecHelper, RAMBlockSize, ReadBuffer}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class RAMCoupledBlockSpec extends AnyFreeSpec with ChiselScalatestTester {
  val p: Parameters = new Config((site, here, up) => {
    case RAMBlockSize => 1024
    case DataWidth => 32
    case ReadBuffer => 8
    case NumberOfNodes => 10
  })

  "RAMCoupledBlock waveform analysis" in {
    test(new RAMCoupledBlock()(p))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

//        val ramHelper = new RAMBankSpecHelper(dut.ram)
//        ramHelper.writeReq(100, 10)

        while (dut.io.top.addr.ready.peek() == false.B) { dut.clock.step() }

        dut.io.top.addr.bits.poke(100.U)
        dut.io.top.addr.valid.poke(true.B)
        dut.clock.step()

        dut.io.top.addr.valid.poke(false.B)
        dut.io.top.data.ready.poke(true.B)
        dut.clock.step()

        while (dut.io.top.data.valid.peek() == false.B) { dut.clock.step() }

//        dut.io.top.data.bits.expect(10.U)
      }
  }

}
