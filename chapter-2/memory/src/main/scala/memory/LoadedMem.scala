package memory

import chisel3._
import chisel3.util.experimental.loadMemoryFromFileInline

class LoadedMem(filename: String) extends Module {
  val io = IO(new Bundle {
    val addr = Input(UInt(10.W))
    val data = Output(UInt(16.W))
  })

  val mem = SyncReadMem(1024, UInt(16.W))
  loadMemoryFromFileInline(mem, filename)

  io.data := mem.read(io.addr)
}
