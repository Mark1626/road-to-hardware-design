import chisel3._
import chisel3.util._

class TestHarness(val invReset: Boolean = true) extends Module {
    val io = IO(new Bundle {
        val led0 = Output(Bool())
        val led1 = Output(Bool())
        val led2 = Output(Bool())
        val led3 = Output(Bool())   
    })

    val customReset = if (invReset) ~reset.asBool() else reset

    withClockAndReset (clock, customReset) {
        val module = Module(new Blinky(25000000))
        module.io <> io
    }
}

class Blinky(val freq: Int) extends Module {
    val io = IO(new Bundle {
        val led0 = Output(Bool())
        val led1 = Output(Bool())
        val led2 = Output(Bool())
        val led3 = Output(Bool())   
    })

    val led = RegInit(0.U)
    val cond = WireInit(false.B)
    val (_, wrap) = Counter(cond, freq / 2)

    cond := true.B

    when (wrap) {
        led := ~led
    }

    io.led0 := led
    io.led1 := !led
    io.led2 := led
    io.led3 := !led
}

object Main extends App {
    (new chisel3.stage.ChiselStage).emitVerilog(
        new TestHarness(true),
        Array.concat(args, Array("-td=./generated"))
    )
}
