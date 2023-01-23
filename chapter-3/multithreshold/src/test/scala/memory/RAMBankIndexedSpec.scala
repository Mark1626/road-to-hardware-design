package memory

import chipsalliance.rocketchip.config.{Config, Parameters}
import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

import java.util.Random
import java.util.concurrent.ThreadLocalRandom

class RAMBankIndexedSpec extends AnyFreeSpec with ChiselScalatestTester  {

  val p: Parameters = new Config((site, here, up) => {
    case RAMBlockSize => 1024
    case DataWidth => 16
    case ReadBuffer => 8
  })

  class RAMBankSpecHelper(dut: RAMBankIndexed) {
    def writeReq(addr: Int, data: Int): Unit = {
      while(dut.io.req.ready.peek() == false.B) { dut.clock.step() }
      //println("Ready to send write request")

      dut.io.req.bits.data.addr.poke(addr.U)
      dut.io.req.bits.data.data.poke(data.U)
      dut.io.req.bits.data.wrena.poke(true.B)
      dut.io.req.valid.poke(true.B)
      dut.clock.step()
    }

    def readReq(addr: Int, idx: Int): (UInt, UInt) = {
      while(dut.io.req.ready.peek() == false.B) { dut.clock.step() }
      //println("Ready to send read request")

      dut.io.req.bits.data.addr.poke(addr.U)
      dut.io.req.bits.data.wrena.poke(false.B)
      dut.io.req.bits.idx.poke(idx)
      dut.io.req.valid.poke(false.B)
      dut.clock.step()

      dut.io.res.ready.poke(true.B)
      dut.clock.step()

      while (dut.io.res.valid.peek() == false.B) { dut.clock.step() }
      (dut.io.res.bits.data.data, dut.io.res.bits.idx)
    }

    def populateRAM(): Unit = {
      for (i <- 0 until 1024) {
        writeReq(i, 10+i)
      }
    }
  }

  def randInt(min: Int, max: Int): Int = {
    ThreadLocalRandom.current().nextInt(min, max)
  }

  "rambank should be able to store and retrieve data" in {
    val n = 10
    test(new RAMBankIndexed(log2Ceil(n))(p)) { dut =>
      val helper = new RAMBankSpecHelper(dut)
      helper.populateRAM()

      for (i <- 0 until 25) {
        val randAddr = randInt(0, 1024)
        var res = helper.readReq(randAddr, 1)
        res._1.expect((10+randAddr).U)
        res._2.expect(1.U)
      }
    }
  }

  "should return response with correct idx as request" in {
    val n = 10
    test(new RAMBankIndexed(log2Ceil(n))(p)) { dut =>
      //      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      val helper = new RAMBankSpecHelper(dut)

      for (i <- 0 until 1024) {
        helper.writeReq(i, 10 + i)
      }

      for (i <- 0 until 25) {
        val randAddr = randInt(0, 10)
        val randIdx = randInt(0, n)

        var res = helper.readReq(randAddr, randIdx)
        res._1.expect((10 + randAddr).U)
        res._2.expect(randIdx.U)
      }
    }
  }

}
