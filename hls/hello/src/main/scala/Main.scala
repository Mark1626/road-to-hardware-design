import chisel3._
object Main extends App {
    (new chisel3.stage.ChiselStage).emitVerilog(
        new TestHarness(invReset = true),
        args
    )
}
