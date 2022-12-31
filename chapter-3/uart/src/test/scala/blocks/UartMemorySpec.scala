package blocks

import chisel3._
import chisel3.util.Enum
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import uart.BaudRates

class UartMemorySpec extends AnyFreeSpec with ChiselScalatestTester {
  val baudrate = 5 // Set a high baud rate for testing
  val waitTime = baudrate * 4

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

    builder.toString()
  }

  // A closure could simplify sending dut
  val sendBit: UartMemory => String => Unit = (dut: UartMemory) => (bits: String) => {
    // Start bit
    dut.uart.rx.poke(0.U)

    for (bit <- bits) {
      dut.clock.step(baudrate)
      dut.uart.rx.poke((bit.toInt - '0').U)
    }

    // Stop bit
    dut.clock.step(baudrate)
    dut.uart.rx.poke(1.U)
  }

  // Assumes just a-z
  val sendChar: UartMemory => Char => Unit = (dut: UartMemory) => (c: Char) => {
    val bits = getBitsStr(c)
    sendBit(dut)(bits)
  }

  val sendStr: UartMemory => String => Unit = (dut: UartMemory) => (str: String) => {
    for (ch <- str) {
      sendChar(dut)(ch)
      dut.clock.step(waitTime)
    }
  }

  "UartMemory move to writing state when w is passed" in {
    test(new UartMemory(baudrate, false, 10, true))
      .withAnnotations(Seq(WriteVcdAnnotation, VerilatorBackendAnnotation)) { dut =>
        val sendStrUart = sendStr(dut)
        dut.clock.setTimeout(100000)

        sendStrUart("w0123456789")
        dut.clock.step(2 * waitTime)
        // No clue on how to assert this, expect looking at VCD
      }
  }

  "UartMemory move to reading state when r is passed" in {
    test(new UartMemory(baudrate, false, 10, true))
      .withAnnotations(Seq(WriteVcdAnnotation, VerilatorBackendAnnotation)) { dut =>
        val sendStrUart = sendStr(dut)
        dut.clock.setTimeout(200000)

        sendStrUart("w0123456789")
//        dut.clock.step(2 * waitTime)

//        val data = Reg
        sendStrUart("r")

        dut.clock.step(100 * waitTime)
        // No clue on how to assert this
        //      dut.state.expect(2.U)
      }
  }

}
