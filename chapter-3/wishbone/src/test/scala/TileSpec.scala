import chisel3._
import chisel3.util._
import chiseltest._
import chipsalliance.rocketchip.config.{Config, Field, Parameters}
import org.scalatest.freespec.AnyFreeSpec

class TileSpec extends AnyFreeSpec with ChiselScalatestTester {
  val busParams = new Config((site, here, up) => {
    case BusWidth => 32
  })

  "Tile Spec" in {
    test(new Tile(3)(busParams))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

      dut.io.res.ready.poke(false.B)

      dut.io.addr.ready.expect(true.B)
      dut.io.addr.bits.poke(100.U)
      dut.io.addr.valid.poke(true.B)

      dut.clock.step()

      dut.io.addr.valid.poke(false.B)
      dut.io.res.ready.poke(true.B)
      dut.clock.step()

      while (!dut.io.res.valid.peekBoolean()) {
        dut.clock.step()
      }

      dut.io.res.bits.expect(800.U)

    }
  }
}
