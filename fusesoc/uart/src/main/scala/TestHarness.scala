import chisel3._
import chisel3.util._
import chisel.lib.uart.{BufferedTx, Rx}

class UartModule extends Module {

}

class TestHarness(
  val frequency: Int = 100000000,
  val baudRate: Int = 115200,
  val invReset: Boolean = true) extends Module {
  val io = IO(new Bundle {
    val txd = Output(UInt(1.W))
    val rxd = Input(UInt(1.W))
    val led0 = Output(Bool())
    val led1 = Output(Bool())
    val led2 = Output(Bool())
    val led3 = Output(Bool())
    val led4 = Output(Bool())
    val led5 = Output(Bool())
    val led6 = Output(Bool())
    val led7 = Output(Bool())
  })

  val customReset = if (invReset) ~reset.asBool else reset

  withClockAndReset(clock, customReset) {
    val tx = Module(new BufferedTx(frequency, baudRate))
    val rx = Module(new Rx(frequency, baudRate))

    io.led0 := true.B
    io.led1 := false.B
    io.led2 := false.B
    io.led3 := false.B
    io.led4 := false.B
    io.led5 := false.B
    io.led6 := false.B
    io.led7 := false.B

//    rx.io.channel.

    io.txd := tx.io.txd
    rx.io.rxd := io.rxd
    tx.io.channel <> rx.io.channel
  }
}
