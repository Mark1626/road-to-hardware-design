package memory

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._
import scala.util.Random

class LoadedMemSpec extends AnyFreeSpec with ChiselScalatestTester {

  "Should be able to fetch values" in {
    test(new LoadedMem("./src/test/data/testdata.hex"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

        for (addr <- 0 until 32) {
          dut.io.addr.poke(addr.U)
          dut.clock.step()
          dut.io.data.expect(addr.U)
        }
    }
  }
}
