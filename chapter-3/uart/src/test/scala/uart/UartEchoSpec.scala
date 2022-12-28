package uart

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

class UartEchoSpec extends AnyFreeSpec with ChiselScalatestTester {

  val baudrate = 868
  val waitTime = baudrate * 4

  "Should be able to echo when value recieved" in {
    test(new UartEcho(baudrate))
      .withAnnotations(Seq(WriteVcdAnnotation, VerilatorBackendAnnotation)) { dut =>

        dut.clock.setTimeout(100000)

        val getBitsStr = (n: Int) => {
          var temp = n
          val builder = new StringBuilder(8)

          for (i <- 0 until 8) {
            if (temp > 0) {
              builder.append(temp % 2)
              temp >>= 1
            } else {
              builder.append(0)
            }
          }

          builder.reverse.toString()
        }

        // Assumes just a-z
        val sendChar = (c: Char) => {
          // Start bit
          dut.io.rx.poke(0.U)

          val bits = getBitsStr(c)
          for (bit <- bits) {
            dut.clock.step(baudrate)
            dut.io.rx.poke((bit.toInt - '0').U)
          }

          // Stop bit
          dut.clock.step(baudrate)
          dut.io.rx.poke(1.U)
        }

        val sendStr = (str: String) => {
          for (ch <- str) {
            sendChar(ch)
            dut.clock.step(waitTime)
          }
        }

        dut.clock.step(baudrate)
        sendStr("OK")
        dut.clock.step(10 * waitTime)

      }
  }
}
