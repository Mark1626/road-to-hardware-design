import chisel3._
import chisel3.internal.sourceinfo.SourceInfo
import diplomacy.nodes.{RenderedEdge, SimpleNodeImp}
import org.chipsalliance.cde.config.Parameters

case class UpwardParam(width: Int)
case class DownwardParam(width: Int)
case class EdgeParam(width: Int)

object AdderNodeImp extends SimpleNodeImp[DownwardParam, UpwardParam, EdgeParam, UInt] {
  def edge(pd: DownwardParam, pu: UpwardParam, p: Parameters, sourceInfo: SourceInfo) = {
    if (pd.width < pu.width) EdgeParam(pd.width) else EdgeParam(pu.width)
  }
  def bundle(e: EdgeParam): UInt = UInt(e.width.W)
  def render(e: EdgeParam): RenderedEdge = RenderedEdge("blue", s"width==${e.width}")
}
