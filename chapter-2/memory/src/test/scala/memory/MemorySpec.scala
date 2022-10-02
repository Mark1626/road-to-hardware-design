package memory

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._
import scala.util.Random

class MemoryTestSpec extends AnyFreeSpec with ChiselScalatestTester {

  "Should be able to store and fetch values" in {
    test(new Memory)
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

        dut.io.wrEna.poke(1.B)
        // dut.clock.step()

        val baseAddr = 100
        val value = 2

        for (i <- 1 until 100) {
          dut.io.wrAddr.poke((baseAddr + i).U)
          dut.io.wrData.poke((value + i).U)

          dut.clock.step()
        }

        dut.io.wrEna.poke(0.B)
        dut.clock.step()

        for (i <- 1 until 100) {
          dut.io.rdAddr.poke((baseAddr + i).U)
          dut.clock.step()
          dut.io.rdData.expect((value + i).U)
        }

      }
  }
}
