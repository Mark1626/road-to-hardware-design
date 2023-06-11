import chisel3._
import chisel3.util._
import chisel.lib.uart.{BufferedTx}

class main extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val clock = Input(Clock())
    val reset = Input(Reset())
    val start_port = Input(Bool())
    val done_port = Output(Bool())
    val return_port = Output(UInt(32.W))
    val TX_READY = Input(Bool())
    val TX_ENABLE = Output(Bool())
    val TX_DATA = Output(UInt(8.W))
  })
  addResource("/top.v")
  addResource("/bambu_putchar.v")
  addResource("/sync_fifo.v")
}

class PLL40 extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val clock_in = Input(Clock())
    val clock_out = Output(Clock())
    val locked = Output(UInt(1.W))
  })
  addResource("/pll.v")
}

class TopModule(val frequency: Int = 25000000,
                val baudRate: Int = 115200) extends Module {
  val io = IO(new Bundle {
    val txd = Output(UInt(1.W))
    val led0 = Output(Bool())
    val led1 = Output(Bool())
    val led2 = Output(Bool())
    val led3 = Output(Bool())
    val led4 = Output(Bool())
    val led5 = Output(Bool())
    val led6 = Output(Bool())
    val led7 = Output(Bool())
  })

  val hls = Module(new main())
  val tx = Module(new BufferedTx(frequency, baudRate))

  val bits = RegInit(0.U(8.W))

  io.led0 := bits(0)
  io.led1 := bits(1)
  io.led2 := bits(2)
  io.led3 := bits(3)
  io.led4 := bits(4)
  io.led5 := bits(5)
  io.led6 := bits(6)
  io.led7 := bits(7)

  // HLS Connections
  hls.io.clock := clock
  hls.io.reset := reset
  hls.io.start_port := ~reset.asBool

  io.led0 := hls.io.done_port
  io.led1 := hls.io.start_port

  io.txd := tx.io.txd

  // Tx connection
  hls.io.TX_READY := tx.io.channel.ready
  tx.io.channel.valid := hls.io.TX_ENABLE
  tx.io.channel.bits := hls.io.TX_DATA

  //
  when(hls.io.TX_ENABLE) {
    bits := hls.io.TX_DATA
  }
}

class TestHarness(
  val frequency: Int = 25000000,
  val baudRate: Int = 115200,
  val invReset: Boolean = true) extends Module {
  
  val io = IO(new Bundle {
    val txd = Output(UInt(1.W))
    val led0 = Output(Bool())
    val led1 = Output(Bool())
    val led2 = Output(Bool())
    val led3 = Output(Bool())
    val led4 = Output(Bool())
    val led5 = Output(Bool())
    val led6 = Output(Bool())
    val led7 = Output(Bool())
  })

  val pll_clock = Wire(Clock())
  val pll = Module(new PLL40())
  pll.io.clock_in := clock
  pll_clock := pll.io.clock_out

  val adjustReset = if (invReset) ~reset.asBool else reset
  withClockAndReset(pll_clock, adjustReset) {
    val top = Module(new TopModule(frequency, baudRate))
    top.io <> io
  }
}
