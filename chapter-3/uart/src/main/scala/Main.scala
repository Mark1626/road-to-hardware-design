
import blocks.{UartEcho, UartMemory}
import chisel3._
import uart.BaudRates

object RegisterVerilog extends App {
  emitVerilog(new UartEcho(BaudRates.B115200, true), Array.concat(args, Array("-td=./out")))
}
