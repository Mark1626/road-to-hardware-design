package memory

import chipsalliance.rocketchip.config.Parameters
import chisel3._
import chisel3.util._
import modules.MultiThresholdParams

class MultiThresholdScratchpad()(implicit val p: Parameters) extends Module
  with RAMBankParams
  with MultiThresholdParams {

  val io = IO(new Bundle {
    val req = Flipped(Decoupled(Indexed(new BusReq(dataWidth, addrWidth), idx_w)))
    val res = Decoupled(Indexed(new BusRes(dataWidth), idx_w))
  })

  val rambank = Module(new RAMBankIndexed())
  val memarbiter = Module(new MemArbiter(nodes, idx_w))

  memarbiter.io.req_in <> io.req

  rambank.io.req <> memarbiter.io.req_out

  memarbiter.io.req_out.ready := io.res.ready

  io.res <> rambank.io.res
}
