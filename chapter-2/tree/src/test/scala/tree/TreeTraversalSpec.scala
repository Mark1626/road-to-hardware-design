package tree

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._
import scala.util.Random

class TreeTraversalSpec extends AnyFreeSpec with ChiselScalatestTester {

  "Should be able to store and fetch values" in {
    test(new TreeTraverse("./src/test/data/maze.hex", 5))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
        dut.io.addr.valid.poke(true.B)
        dut.io.addr.bits(0).poke(4.U)
        dut.io.addr.bits(1).poke(16.U)

        dut.clock.step()

        dut.io.addr.valid.poke(false.B)

        for (i <- 0 until 5) {
          dut.clock.step()
        }

        dut.io.pathData.valid.expect(true.B)
        for (i <- 0 until 5) {
          dut.io.pathData.bits(i).expect(4)
        }

        dut.clock.step()
        dut.io.pathData.valid.expect(false.B)
    }
  }
}
