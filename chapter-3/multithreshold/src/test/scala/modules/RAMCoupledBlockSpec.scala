package modules

import chipsalliance.rocketchip.config.{Config, Parameters}
import chisel3._
import chisel3.util._
import chiseltest._
import memory.{ArbQueueDepth, BusWidth, DataWidth, NumberOfNodes, RAMBlockSize, ReadBuffer}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class RAMCoupledBlockSpecHelper(dut: RAMCoupledBlock) {
  def writeReq(addr: Int, data: Int): Unit = {
    while (dut.io.top.req.ready.peek() == false.B) {
      dut.clock.step()
    }

    dut.io.top.req.bits.addr.poke(addr.U)
    dut.io.top.req.bits.data.poke(data.U)
    dut.io.top.req.bits.wrena.poke(true.B)
    dut.io.top.req.valid.poke(true.B)
    dut.clock.step()

    dut.io.top.req.valid.poke(false.B)
    dut.clock.step()
  }

  def readReq(addr: Int, idx: Int): UInt = {
    while (dut.io.top.req.ready.peek() == false.B) {
      dut.clock.step()
    }

    dut.io.top.req.bits.addr.poke(addr.U)
    dut.io.top.req.bits.wrena.poke(false.B)
    dut.io.top.req.valid.poke(true.B)
    dut.clock.step()

    dut.io.top.req.valid.poke(false.B)
    dut.io.top.res.ready.poke(true.B)
    dut.clock.step()

    while (dut.io.top.res.valid.peek() == false.B) {
      dut.clock.step()
    }

    val res = dut.io.top.res.bits.data

    dut.io.top.res.ready.poke(false.B)
    dut.clock.step()

    res
  }

  def populateRAM(): Unit = {
    for (i <- 0 until 1024) {
      writeReq(i, 10 + i)
    }
  }
}


class RAMCoupledBlockSpec extends AnyFreeSpec with ChiselScalatestTester {
  val p: Parameters = new Config((site, here, up) => {
    case RAMBlockSize => 1024
    case DataWidth => 32
    case ReadBuffer => 8
    case NumberOfNodes => 10
    case BusWidth => 32
  })

  "RAMCoupledBlock waveform analysis" in {
    test(new RAMCoupledBlock()(p))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

        val helper = new RAMCoupledBlockSpecHelper(dut)
        helper.writeReq(100, 10)
        helper.writeReq(101, 11)

//        while (dut.io.top.req.ready.peek() == false.B) { dut.clock.step() }


      }
  }

}
