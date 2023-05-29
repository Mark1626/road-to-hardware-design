import chisel3._
import chisel3.util._
import chisel.lib.uart.{BufferedTx, Rx}

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

    val bits = RegInit(0.U(8.W))

    io.led0 := bits(0)
    io.led1 := bits(1)
    io.led2 := bits(2)
    io.led3 := bits(3)
    io.led4 := bits(4)
    io.led5 := bits(5)
    io.led6 := bits(6)
    io.led7 := bits(7)

    io.txd := tx.io.txd
    rx.io.rxd := io.rxd

    val commandDecoder = Module(new CommandDecoder())

    commandDecoder.io.in <> rx.io.channel
    tx.io.channel <> rx.io.channel
    rx.io.channel.ready := tx.io.channel.ready && commandDecoder.io.in.ready

    when(rx.io.channel.valid) {
      bits := rx.io.channel.bits
    }
  }
}
