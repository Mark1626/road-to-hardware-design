
import chisel3._
object RegisterVerilog extends App {
  emitVerilog(new Top(100000000, 115200), Array.concat(args, Array("-td=./out")))
}
