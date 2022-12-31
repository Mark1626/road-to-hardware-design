package blocks

import chisel3._
import uart.{BaudRates, HasUartIO}

class UartEcho(
                val baudrate: Int = BaudRates.B115200,
                val rstn: Boolean = true) extends Module with HasUartIO {
  uart_tx_m.io.start  := uart_rx_m.io.rcv
  uart_tx_m.io.data   := uart_rx_m.io.data
}
