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
