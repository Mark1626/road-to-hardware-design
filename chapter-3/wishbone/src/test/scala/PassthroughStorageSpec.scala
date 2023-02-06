import chisel3._
import chisel3.util._
import chiseltest._
import chipsalliance.rocketchip.config.{Config, Field, Parameters}
import org.scalatest.freespec.AnyFreeSpec

class PassthroughStorageSpec extends AnyFreeSpec with ChiselScalatestTester {
  val busParams = new Config((site, here, up) => {
    case BusWidth => 32
  })

  "Passthrough Storage Spec" in {
    test(new PassthroughStorage()(busParams))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.io.bus.stb.poke(true.B)
      dut.io.bus.cyc.poke(true.B)
      dut.io.bus.addr.poke(100.U)
      dut.clock.step()

      while (dut.io.bus.ack.peek() == false.B) {
        dut.clock.step()
      }

      val rd = dut.io.bus.data_rd

      dut.io.bus.stb.poke(false.B)
      dut.io.bus.cyc.poke(false.B)
      dut.clock.step()

      dut.io.bus.ack.expect(false.B)

      rd.expect(100.U)
    }
  }
}
