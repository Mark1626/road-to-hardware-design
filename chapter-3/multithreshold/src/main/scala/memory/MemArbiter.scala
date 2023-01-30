package memory

import chipsalliance.rocketchip.config.{Field, Parameters}
import chisel3._
import chisel3.util._

class MemArbiterIO(val idx_w: Int)(implicit val p: Parameters) extends Bundle with RAMBankParams {
  val req_in = Flipped(Decoupled(Indexed(new BusReq(dataWidth, addrWidth), idx_w)))
  val req_out = Decoupled(Indexed(new BusReq(dataWidth, addrWidth), idx_w))
}

case object ArbQueueDepth extends Field[Int]

class MemArbiter(val max_streams: Int, val idx_w: Int)(implicit val p: Parameters) extends Module with RAMBankParams {
  val io = IO(new MemArbiterIO(idx_w))

  val depth = p(ArbQueueDepth)
  val que = Seq.fill(max_streams)(Module(new Queue(new BusReq(dataWidth, addrWidth), depth)))

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

  val arbiter = Module(new RRArbiter(new BusReq(dataWidth, addrWidth), max_streams))
  for (i <- 0 until max_streams) {
    arbiter.io.in(i) <> que(i).io.deq
  }

  arbiter.io.out.ready := io.req_out.ready
  io.req_out.valid := arbiter.io.out.valid
  io.req_out.bits.data <> arbiter.io.out.bits
  io.req_out.bits.idx := arbiter.io.chosen
}
