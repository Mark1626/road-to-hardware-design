
import chisel3._
import uart.UartEcho

object RegisterVerilog extends App {
  emitVerilog(new UartEcho(), Array.concat(args, Array("-td=./out")))
}
