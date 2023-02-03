package memory

import chipsalliance.rocketchip.config.{Field, Parameters}
import chisel3._
import chisel3.util._

class MemArbiterIO()(implicit val p: Parameters) extends Bundle with BusNodesParams {
  val req_in = Flipped(Decoupled(Indexed(new BusReq(busWidth), idx_w)))
  val req_out = Decoupled(Indexed(new BusReq(busWidth), idx_w))
}

case object ArbQueueDepth extends Field[Int]

class MemArbiter(val max_streams: Int)(implicit val p: Parameters) extends Module with BusNodesParams {
  val io = IO(new MemArbiterIO())

  val depth = p(ArbQueueDepth)
  val que = Seq.fill(max_streams)(Module(new Queue(new BusReq(busWidth), depth)))

  val cur_idx = io.req_in.bits.idx

  io.req_in.ready := false.B
  que.zipWithIndex.foreach { case(q, idx) =>
    q.io.enq.bits := io.req_in.bits.data
    when (idx.U =/= cur_idx) {
      q.io.enq.valid := false.B
    } .otherwise {
      q.io.enq.valid := io.req_in.valid
      io.req_in.ready := q.io.enq.ready
    }
  }

  val arbiter = Module(new RRArbiter(new BusReq(busWidth), max_streams))
  for (i <- 0 until max_streams) {
    arbiter.io.in(i) <> que(i).io.deq
  }

  arbiter.io.out.ready := io.req_out.ready
  io.req_out.valid := arbiter.io.out.valid
  io.req_out.bits.data <> arbiter.io.out.bits
  io.req_out.bits.idx := arbiter.io.chosen
}
