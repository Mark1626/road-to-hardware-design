
import chisel3._

object RegisterVerilog extends App {
  emitVerilog(new TestHarness()((new AdderConfig).toInstance), Array.concat(args, Array("-td=./out")))

}
