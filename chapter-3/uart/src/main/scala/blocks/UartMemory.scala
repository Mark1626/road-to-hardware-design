package blocks

import chisel3._
import chisel3.util._
import uart.{BaudRates, HasUartIO}
import memory.Memory


class UartMemory(
                  val baudrate: Int = BaudRates.B115200,
                  val rstn: Boolean = true,
                  val sz: Int = 1024,
                  val testing: Boolean = false
                ) extends Module with HasUartIO {

  uart_tx_m.io.start := false.B

  val mem = Module(new Memory(sz))
  mem.io.wrEna  := false.B
  mem.io.wrAddr := DontCare
  mem.io.wrData := DontCare
  mem.io.rdAddr := DontCare
  mem.io.rdData := DontCare

  private val data                  = RegInit(0.U(8.W))
  private val cmd                   = RegInit(0.U)

  val idle :: writing :: reading :: done :: Nil = Enum(4)
  private val state                 = RegInit(idle)

  private val writeAddrOffset       = Counter(sz)
  private val readAddrOffset        = Counter(sz)

  cmd     := 0.U
  when (uart_rx_m.io.rcv) {
    data  := uart_rx_m.io.data
    cmd   := uart_rx_m.io.data
  }

  when (state === idle) {
    when (cmd === 114.U(8.W)) {
      state := reading
      readAddrOffset.reset()
    } .elsewhen (cmd === 119.U(8.W)) {
      state := writing
      writeAddrOffset.reset()
    }
  } .elsewhen (state === writing) {
    mem.io.wrEna        := true.B
    mem.io.wrAddr       := writeAddrOffset.value
    mem.io.wrData       := data

    when(uart_rx_m.io.rcv) {
      writeAddrOffset.inc()
    }

    when (writeAddrOffset.value === (sz-1).U) {
      state             := idle
    }

  } .elsewhen (state === reading) {
    uart_tx_m.io.start  := uart_tx_m.io.ready
    mem.io.rdAddr       := readAddrOffset.value
    uart_tx_m.io.data   := mem.io.rdData

    // Increment when transmission has completed
    when(uart_tx_m.io.start) {
      readAddrOffset.inc()
    }

    when(readAddrOffset.value === (sz - 1).U) {
      state := idle
    }
  }

  if (testing) {
    val io = IO(new Bundle {
      val state = Output(UInt(2.W))
    })
    io.state := state
  }
}
