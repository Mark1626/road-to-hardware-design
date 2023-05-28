import chisel.lib.uart.{BufferedTx, Rx}
import chisel3._

class Top(val frequency: Int, val baudRate: Int, val invReset: Boolean = true) extends Module {
  val io = IO(new Bundle {
    val txd = Output(UInt(1.W))
    val rxd = Input(UInt(1.W))
  })

  val customReset = if (invReset) ~reset.asBool else reset

  withClockAndReset(clock, customReset) {

    val tx = Module(new BufferedTx(frequency, baudRate))
    val rx = Module(new Rx(frequency, baudRate))

    io.txd := tx.io.txd
    rx.io.rxd := io.rxd
    tx.io.channel <> rx.io.channel
  }
}
