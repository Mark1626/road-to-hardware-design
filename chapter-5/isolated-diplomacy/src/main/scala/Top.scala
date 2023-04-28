
import chisel3._
import diplomacy.lazymodule.{LazyModule, LazyModuleImp}
import org.chipsalliance.cde.config.{Field, Parameters}

case object NumOperands extends Field[Int]
case object BitWidth extends Field[Int]

class AdderTestHarness()(implicit p: Parameters) extends LazyModule {
  val numOperands = p(NumOperands)
  val bitWidth = p(BitWidth)

  val adder = LazyModule(new Adder)
  val drivers = Seq.fill(numOperands) { LazyModule(new AdderDriver(width = bitWidth, numOutputs = 2)) }

  val monitor = LazyModule(new AdderMonitor(width = bitWidth, numOperands = numOperands))

  drivers.foreach( driver => adder.node := driver.node )

  drivers.zip(monitor.nodeSeq).foreach {
    case (driver, monitorNode) => monitorNode := driver.node
  }
  monitor.nodeSum := adder.node

  lazy val module = new LazyModuleImp(this) {
    val io = IO(new Bundle {
      val done = Output(Bool())
    })

    when (monitor.module.io.error) {
      printf("error")
    }
    io.done := monitor.module.io.error
  }
  override lazy val desiredName = "AdderTestHarness"
}

class TestHarness()(implicit p: Parameters) extends Module {
  val io = IO(new Bundle {
    val success = Output(Bool())
  })

  val ldut = LazyModule(new AdderTestHarness())
  val dut = Module(ldut.module)

  io.success := dut.io.done
}
