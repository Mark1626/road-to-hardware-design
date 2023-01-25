package modules

import chisel3._
import chisel3.util._
import bus.WishboneSlave
import chipsalliance.rocketchip.config.{Field, Parameters}
import chisel3.experimental.FixedPoint
import memory.{MemArbiter, RAMBankIndexed, RAMBankParams}

case object NumberOfNodes extends Field[Int]

trait MultiThresholdParams extends ThresholdParams {
  implicit val p: Parameters
  val nodes = p(NumberOfNodes)
  val idx_w = log2Ceil(nodes)
}

class MultiThresholdDetectorIO()(implicit val p: Parameters) extends Bundle
  with MultiThresholdParams {
  val in = Flipped(Decoupled(FixedPoint(w.W, bp.BP)))
  val out = Decoupled(UInt(log2Ceil(nodes).W))
}

class MultiThresholdDetector()(implicit val p: Parameters) extends Module
  with MultiThresholdParams
  with RAMBankParams {

  val io = IO(new MultiThresholdDetectorIO())

  val rambank = Module(new RAMBankIndexed())
  val memarbiter = Module(new MemArbiter(nodes, idx_w))

  val detectors = Seq.range(0, nodes).map(nodeIdx =>
    Module(new ThresholdDetectorChiselModule(nodeIdx))
  )

}
