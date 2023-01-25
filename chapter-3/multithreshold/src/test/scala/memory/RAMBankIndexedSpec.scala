package memory

import chipsalliance.rocketchip.config.{Config, Parameters}
import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

import java.util.Random
import java.util.concurrent.ThreadLocalRandom


class RAMBankSpecHelper(dut: RAMBankIndexed) {
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

// TODO: Revisit the VCD waveform, it kind of looks sketchy as signals
//       set in the test occur at falling edge and the module uses the rising edge
class RAMBankIndexedSpec extends AnyFreeSpec with ChiselScalatestTester  {
  val n = 10
  val p: Parameters = new Config((site, here, up) => {
    case RAMBlockSize => 1024
    case DataWidth => 16
    case ReadBuffer => 8
    case NumberOfNodes => 10
  })

  def randInt(min: Int, max: Int): Int = {
    ThreadLocalRandom.current().nextInt(min, max)
  }

  "rambank waveform analysis" in {
    test(new RAMBankIndexed()(p))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      val helper = new RAMBankSpecHelper(dut)

      for (i <- 0 until 4) {
        helper.writeReq(i, 10 + i)
      }

      for (i <- 0 until 4) {
        val idx = (3-i)
        var res = helper.readReq(idx, 1)
        res._1.expect((10 + idx).U)
        res._2.expect(1.U)
      }
    }
  }

  "rambank should be able to store and retrieve data" in {
    test(new RAMBankIndexed()(p)) { dut =>
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

  "rambank should return response with correct idx as request" in {
    test(new RAMBankIndexed()(p)) { dut =>
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

  // TODO: Add test when request_ready is false, this happens when the queue is full

}
