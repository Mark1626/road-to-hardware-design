package modules

import bus.Wishbone
import chipsalliance.rocketchip.config.{Field, Parameters}
import chisel3._
import chisel3.util._
import chisel3.experimental.{BaseModule, FixedPoint}

case object FixedPointWidth   extends Field[Int]
case object BinaryPointWidth  extends Field[Int]

trait ThresholdParams {
  implicit val p: Parameters
  val w   = p(FixedPointWidth)
  val bp  = p(BinaryPointWidth)
}

class ThresholdDetectorIO()(implicit val p: Parameters) extends Bundle with ThresholdParams {
  val in  = Flipped(Decoupled(FixedPoint(w.W, bp.BP)))
  val threshold = Input(FixedPoint(w.W, bp.BP))
  val out = Decoupled(Bool())
}

trait HasThresholdDetectorIO extends BaseModule {
  implicit val p: Parameters

  val io = IO(new ThresholdDetectorIO()(p))
}

class ThresholdDetectorChiselModule(val offset: Int = 0)(implicit val p: Parameters) extends Module
  with ThresholdParams
  with HasThresholdDetectorIO {

  val s_idle :: s_busy :: s_done :: Nil = Enum(3)

  val state = RegInit(s_idle)

  val v = Reg(FixedPoint(w.W, bp.BP))
  val res = Reg(Bool())
  val done = RegInit(false.B)

  io.in.ready := state === s_idle
  io.out.valid := state === s_done
  io.out.bits := res

  when(state === s_idle && io.in.valid) {
    state := s_busy
    res := false.B
    done := false.B
  }.elsewhen(state === s_busy && done) {
    state := s_done
  }.elsewhen(state === s_done && io.out.ready) {
    state := s_idle
  }

  when(state === s_idle && io.in.valid) {
    v := io.in.bits
  }.elsewhen(state === s_busy) {
    when(v > io.threshold) {
      res := true.B
    }.otherwise {
      res := false.B
    }
    done := true.B
  }
}

class WishboneThresholdDetector()(implicit val p: Parameters) extends Module {
  val io = IO(new Bundle{
    val bus = new Wishbone(N=32)
  })


}
