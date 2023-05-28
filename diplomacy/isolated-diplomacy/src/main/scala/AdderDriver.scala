import org.chipsalliance.cde.config.{Parameters}
import chisel3._
import chisel3.util.random.FibonacciLFSR
import diplomacy.lazymodule.{LazyModule, LazyModuleImp}
import diplomacy.nodes.{SourceNode}

class AdderDriverNode(widths: Seq[DownwardParam])(implicit valName: sourcecode.Name) extends SourceNode(AdderNodeImp)(widths)

class AdderDriver(width: Int, numOutputs: Int)(implicit p: Parameters) extends LazyModule {
  val node = new AdderDriverNode(Seq.fill(numOutputs)(DownwardParam(width)))
  lazy val module = new LazyModuleImp(this) {
    val finalWidths = node.edges.out.map(_.width)
    require(finalWidths.forall(_ == finalWidths.head))
    val finalWidth = finalWidths.head

    val randomNum = FibonacciLFSR.maxPeriod(finalWidth)

    node.out.foreach { case (adden, _) => adden := randomNum }
  }

  override lazy val desiredName = "AdderDriver"
}
