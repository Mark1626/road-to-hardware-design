import chisel3.util._
import chisel3._

class CommandDecoder extends Module {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(UInt(8.W)))
    val out = Decoupled(UInt(32.W))
  })

  val regs = Reg(Vec(4, UInt(8.W)))
  val inc = WireDefault(false.B)
  val (_, wrap) = Counter(inc, 4)

  val s_idle :: s_busy :: s_done :: Nil = Enum(3)
  val state = RegInit(s_idle)

  io.in.ready   := state =/= s_done
  io.out.valid  := state === s_done
  io.out.bits   := Cat(regs(3), regs(2), regs(1), regs(0))

  inc := false.B

  when (io.in.fire) {
    when (state === s_idle) {
      state := s_busy
    }
    when (state === s_busy && wrap) {
      state := s_done
    }
    regs(3) := regs(2)
    regs(2) := regs(1)
    regs(1) := regs(0)
    regs(0) := io.in.bits

    inc := true.B
  }

  when (io.out.fire && state === s_done) {
    state := s_idle
  }
}
