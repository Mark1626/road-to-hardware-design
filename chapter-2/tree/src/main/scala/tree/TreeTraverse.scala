package tree

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFileInline


// Store the tree
class TreeTraverse(filename: String, pathlen: Int = 10) extends Module {
  val io = IO(new Bundle {
    val addr     = Flipped(Valid(Vec(2, UInt(8.W))))
    val pathData    = Irrevocable(Vec(pathlen, UInt(8.W)))
  })

  io.pathData.valid := false.B
  io.pathData.bits  := DontCare

  // 1Kb
  val mem = SyncReadMem(1024, UInt(8.W))
  loadMemoryFromFileInline(mem, filename)

  val idle :: busy :: done :: Nil = Enum(3)

  val state         = RegInit(idle)
  val nodeAddr      = RegInit(0.U(8.W))

  val path          = Reg(Vec(pathlen, UInt(8.W)))
  val step          = Counter(pathlen + 1)
  
  val startAddr     = RegInit(0.U(8.W))
  val endAddr       = RegInit(0.U(8.W))

  when (state === idle) {
    when (io.addr.valid) {
      startAddr := io.addr.bits(0)
      endAddr   := io.addr.bits(1)
      nodeAddr  := io.addr.bits(0)

      path(0)   := io.addr.bits(0)
      step.inc()

      state     := busy
    }
  } .elsewhen (state === busy) {

    val nodeType = mem.read(nodeAddr)

    // Logic to be added here

    path(step.value) := nodeAddr

    step.inc()

    when (step.value >= pathlen.U) {
      state := done
    }

  } .elsewhen (state === done) {
    io.pathData.valid := true.B
    io.pathData.bits  := path
    state := idle
  }

}
