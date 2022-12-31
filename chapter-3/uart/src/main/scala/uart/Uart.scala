package uart

import chisel3._
import chisel3.util._
import chisel3.experimental.{IntParam, BaseModule}

class uart_rx(val baud: Int) extends BlackBox(Map("BAUDRATE" -> IntParam(baud))) with HasBlackBoxResource {
  val io = IO(new Bundle {
    val clk     = Input(Clock())
    val rstn    = Input(Reset())
    val rx      = Input(Bool())
    val rcv     = Output(Bool())
    val data    = Output(UInt(8.W))
  })
  addResource("/vsrc/uart_rx.v")
  addResource("/vsrc/baudgen_rx.v")
  addResource("/vsrc/baudgen.vh")
}

class uart_tx(val baud: Int) extends BlackBox(Map("BAUDRATE" -> IntParam(baud))) with HasBlackBoxResource {
  val io = IO(new Bundle {
    val clk     = Input(Clock())
    val rstn    = Input(Reset())
    val start   = Input(Bool())
    val data    = Input(UInt(8.W))
    val tx      = Output(Bool())
    val ready   = Output(Bool())
  })
  addResource("/vsrc/uart_tx.v")
  addResource("/vsrc/baudgen_tx.v")
  addResource("/vsrc/baudgen.vh")
}

class UartIO extends Bundle {
  val rx      = Input(Bool())
  val tx      = Output(Bool())
}

trait HasUartIO extends Module {
  val baudrate: Int
  val rstn: Boolean
  val uart = IO(new UartIO)

  val uart_rx_m = Module(new uart_rx(baudrate))
  val uart_tx_m = Module(new uart_tx(baudrate))

  uart_rx_m.io.clk := clock
  uart_rx_m.io.rx := uart.rx

  uart_tx_m.io.clk := clock
  uart.tx := uart_tx_m.io.tx

  if (rstn) {
    uart_rx_m.io.rstn := reset
    uart_tx_m.io.rstn := reset
  } else {
    // This is a work around for the Blackbox UART
    val rst = RegInit(false.B)
    rst := reset
    uart_rx_m.io.rstn := ~rst
    uart_tx_m.io.rstn := ~rst
  }
}
