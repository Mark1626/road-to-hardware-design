package uart

import chisel3._

class UartEcho(val baudrate: Int = 868) extends Module {
  val io = IO(new Bundle {
    val rx = Input(Bool())
    val tx = Output(Bool())
  })

  val uart_rx_m = Module(new uart_rx(baudrate))
  val uart_tx_m = Module(new uart_tx(baudrate))

  uart_rx_m.io.rx     := io.rx
  uart_rx_m.io.clk    := clock
  uart_rx_m.io.rstn   := reset

  uart_tx_m.io.start  := uart_rx_m.io.rcv
  uart_tx_m.io.data   := uart_rx_m.io.data
  uart_tx_m.io.clk    := clock
  uart_tx_m.io.rstn   := reset

  io.tx               := uart_tx_m.io.tx

}
