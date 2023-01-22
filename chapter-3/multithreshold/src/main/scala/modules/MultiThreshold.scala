package modules

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

class ThresholdDetectorIO(w: Int, bp: Int) extends Bundle {
  val in  = Flipped(Decoupled(FixedPoint(w.W, bp.BP)))
  val out = Decoupled(Bool())
}

trait HasThresholdDetectorIO extends BaseModule {
  val w: Int
  val bp: Int

  val io = IO(new ThresholdDetectorIO(w, bp))
}

class MultiThresholdRAMIO()(implicit val p: Parameters) extends Bundle
  with RAMParams {
  val rdAddr = Flipped(Decoupled(UInt(addrWidth.W)))
  val rdData = Decoupled(UInt(dataWidth.W))
}

trait HasMultiThresholdRAMIO extends BaseModule {
  implicit val p: Parameters
  val mem = IO(new MultiThresholdRAMIO()(p))
}

class ThresholdDetectorChiselModule(val offset: Int)(implicit val p: Parameters) extends Module
  with ThresholdParams
  with HasThresholdDetectorIO
  with HasMultiThresholdRAMIO {

  val s_init :: s_idle :: s_busy :: s_done :: Nil = Enum(4)

  val si_idle :: si_busy :: si_done :: Nil = Enum(2)
  val initState = RegInit(si_idle)

  val state = RegInit(s_init)

  val v = Reg(FixedPoint(w.W, bp.BP))
  val res = Reg(Bool())
  val done = RegInit(false.B)

  val threshold = RegInit(FixedPoint(w.W, bp.BP))
  val initDone = RegInit(false.B)

  io.in.ready := state === s_idle
  io.out.valid := state === s_done
  io.out.bits := res

  when(state === s_idle && io.in.valid) {
    state := s_busy
    res := 0.F(0.BP)
    done := false.B
  }.elsewhen(state === s_busy && done) {
    state := s_done
  }.elsewhen(state === s_done && io.out.ready) {
    state := s_idle
  } .elsewhen(state === s_init && initDone) {
    state := s_idle
  }

  when(state === s_idle && io.in.valid) {
    v := io.in.bits
  }.elsewhen(state === s_busy) {
    when(threshold > v) {
      res := 1.F(0.BP)
    }.otherwise {
      res := 0.F(0.BP)
    }
    done := true.B
  }.elsewhen(state === s_init) {

    when (mem.rdAddr.ready) {
      mem.rdAddr  := offset.U
    }
    // Need a transaction here

    threshold   := mem.rdData
    initDone    := true.B
  }
}
