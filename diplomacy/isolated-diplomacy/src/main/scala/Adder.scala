
import chisel3._
import diplomacy.lazymodule.{LazyModule, LazyModuleImp}
import diplomacy.nodes.NexusNode
import org.chipsalliance.cde.config.{Config, Parameters}

class AdderNode(dFn: Seq[DownwardParam] => DownwardParam,
                uFn: Seq[UpwardParam] => UpwardParam)
               (implicit valName: sourcecode.Name)
  extends NexusNode(AdderNodeImp)(dFn, uFn)

class Adder()(implicit p: Parameters) extends LazyModule {
  val node = new AdderNode(
    {
      case dps: Seq[DownwardParam] =>
        require(dps.forall(dp => dp.width == dps.head.width), "inward, downward width must be same")
        dps.head
    },
    {
      case ups: Seq[UpwardParam] =>
        require(ups.forall(up => up.width == ups.head.width), "outward, upward width must be same")
        ups.head
    }
  )

  lazy val module = new LazyModuleImp(this) {
    require(node.in.size >= 2)
    node.out.head._1 := node.in.unzip._1.reduce(_ + _)
  }

  override lazy val desiredName = "Adder"
}
