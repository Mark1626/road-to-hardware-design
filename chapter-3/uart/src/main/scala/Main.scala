
import blocks.{UartEcho, UartMemory}
import chisel3._
import uart.BaudRates

object RegisterVerilog extends App {
  emitVerilog(new UartMemory(BaudRates.B115200, true, 10), Array.concat(args, Array("-td=./out")))
}
