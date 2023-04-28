
import chisel3._
import diplomacy.lazymodule.{LazyModule, LazyModuleImp}
import org.chipsalliance.cde.config.{Parameters}
import diplomacy.nodes.SinkNode

class AdderMonitorNode(width: UpwardParam)(implicit valName : sourcecode.Name)
  extends SinkNode(AdderNodeImp)(Seq(width))

class AdderMonitor(width:Int, numOperands: Int)(implicit p: Parameters) extends LazyModule {
  val nodeSeq = Seq.fill(numOperands) { new AdderMonitorNode(UpwardParam(width)) }
  val nodeSum = new AdderMonitorNode(UpwardParam(width))
  lazy val module = new LazyModuleImp(this) {
    val io = IO(new Bundle {
      val error = Output(Bool())
    })

    printf(nodeSeq.map(node => p"${node.in.head._1}")
      .reduce(_ + p" + " + _) + p" = ${nodeSum.in.head._1}\n")

    io.error := nodeSum.in.head._1 =/= nodeSeq.map(_.in.head._1).reduce(_ + _)
  }

  override lazy val desiredName = "AdderMonitor"
}
