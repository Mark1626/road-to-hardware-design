package tree

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFileInline

class AddrInput extends Bundle {
  val startAddr = UInt(8.W)
  val endAddr   = UInt(8.W)
}

// Store the tree
class TreeTraverse(filename: String, pathlen: Int = 10) extends Module {
  val io = IO(new Bundle {
    val addr        = Flipped(Decoupled(new AddrInput()))
    val pathData    = Valid(Vec(pathlen, UInt(8.W)))
  })

  // 1Kb
  val mem = SyncReadMem(1024, UInt(8.W))
  loadMemoryFromFileInline(mem, filename)

  val idle :: traverse :: done :: Nil = Enum(3)

  val state         = RegInit(idle)
  // Do I need this or can I just do a clear on reset?
  val solved        = RegInit(false.B)
  val nodeAddr      = RegInit(0.U(8.W))

  val path          = Reg(Vec(pathlen, UInt(8.W)))
  val step          = Counter(pathlen + 1)
  
  val startAddr     = RegInit(0.U(8.W))
  val endAddr       = RegInit(0.U(8.W))

  io.addr.ready     := state === idle
  io.pathData.valid := !(state === traverse) && solved
  io.pathData.bits  := DontCare

  when (io.addr.valid && state === idle) {
    startAddr         := io.addr.bits.startAddr
    endAddr           := io.addr.bits.endAddr
    nodeAddr          := io.addr.bits.startAddr

    path(0)           := io.addr.bits.startAddr
    step.inc()

    // io.pathData.valid := false.B
    state             := traverse
  } .elsewhen (state === traverse) {
    val nodeType = mem.read(nodeAddr)
    // Logic
    path(step.value)  := nodeAddr
    step.inc()

    when (step.value >= pathlen.U) {
      state := done
    }
  } .elsewhen (state === done) {
    io.pathData.valid := true.B
    io.pathData.bits  := path
    state             := idle
    solved            := true.B
  }

}
