package memory

import chipsalliance.rocketchip.config.{Config, Parameters}
import chisel3._
import chisel3.util._
import chiseltest._
import modules.{BinaryPointWidth, FixedPointWidth, NumberOfNodes}
import org.scalatest.freespec.AnyFreeSpec

class Helper(dut: MultiThresholdScratchpad) {
  def writeReq(addr: Int, data: Int): Unit = {
    while (dut.io.req.ready.peek() == false.B) {
      dut.clock.step()
    }

    dut.io.req.bits.data.addr.poke(addr.U)
    dut.io.req.bits.data.data.poke(data.U)
    dut.io.req.bits.data.wrena.poke(true.B)
    dut.io.req.valid.poke(true.B)
    dut.clock.step()

    dut.io.req.valid.poke(false.B)
    dut.clock.step()
  }

  def readReq(addr: Int, idx: Int): (UInt, UInt) = {
    while (dut.io.req.ready.peek() == false.B) {
      dut.clock.step()
    }

    dut.io.req.bits.data.addr.poke(addr.U)
    dut.io.req.bits.data.wrena.poke(false.B)
    dut.io.req.bits.idx.poke(idx)
    dut.io.req.valid.poke(true.B)
    dut.clock.step()

    dut.io.req.valid.poke(false.B)
    dut.io.res.ready.poke(true.B)
    dut.clock.step()

    while (dut.io.res.valid.peek() == false.B) {
      dut.clock.step()
    }

    val res = (dut.io.res.bits.data.data, dut.io.res.bits.idx)

    dut.io.res.ready.poke(false.B)
    dut.clock.step()

    res
  }

  def populateRAM(): Unit = {
    for (i <- 0 until 1024) {
      writeReq(i, 10 + i)
    }
  }

}

//class MultiThresholdScratchpadSpec extends AnyFreeSpec with ChiselScalatestTester  {
//  val p: Parameters = new Config((site, here, up) => {
//    case RAMBlockSize => 1024
//    case DataWidth => 16
//    case ReadBuffer => 8
//    case ArbQueueDepth => 4
//    case FixedPointWidth => 8
//    case BinaryPointWidth => 4
//    case NumberOfNodes => 10
//  })
//
//  "scratchpad waveform analysis" in {
//    val n = 10
//    test(new MultiThresholdScratchpad()(p))
//      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
//        val helper = new Helper(dut)
//
//        helper.writeReq(100, 100)
//        helper.writeReq(101, 101)
//
//        val res1 = helper.readReq(100, 1)
//        val res2 = helper.readReq(101, 2)
//        val res3 = helper.readReq(100, 2)
//
//        res1._1.expect((100).U)
//        res1._2.expect(1.U)
//
//        res2._1.expect((101).U)
//        res2._2.expect(2.U)
//
//        res3._1.expect((100).U)
//        res3._2.expect(2.U)
//      }
//  }
//
//}
