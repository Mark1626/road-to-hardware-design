import chisel3._
import chisel3.util._
import chiseltest._
import chipsalliance.rocketchip.config.{Config, Field, Parameters}
import org.scalatest.freespec.AnyFreeSpec

class ReaderSpec extends AnyFreeSpec with ChiselScalatestTester {
  val busParams = new Config((site, here, up) => {
    case BusWidth => 32
  })

  "Reader spec" in {
    test(new Reader(100)(busParams))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
        dut.io.addr.valid.poke(false.B)
        dut.io.addr.ready.expect(true.B)
        //dut.io.down.bus.stb.expect(false.B)
        dut.io.down.bus.cyc.expect(false.B)

        dut.io.addr.bits.poke(100.U)
        dut.io.addr.valid.poke(true.B)

        dut.clock.step()

        dut.io.addr.valid.poke(false.B)

        dut.io.down.bus.addr.expect(200.U)
        //dut.io.down.bus.stb.expect(true.B)
        dut.io.down.bus.cyc.expect(true.B)
        dut.io.down.bus.data_rd.poke(200.U)
        dut.io.down.bus.ack.poke(true.B)
        dut.io.data.ready.poke(true.B)

        dut.clock.step()
        dut.io.down.bus.stb.expect(false.B)
        dut.io.down.bus.cyc.expect(false.B)
        dut.io.down.bus.ack.poke(false.B)

        dut.io.data.bits.expect(200.U)

        dut.clock.step()
      }
  }
}
