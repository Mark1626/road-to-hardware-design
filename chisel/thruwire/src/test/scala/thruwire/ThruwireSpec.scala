package thruwire

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._


class ThruwireSpec extends AnyFreeSpec with ChiselScalatestTester {
  "Should be able to passthrough data recieved" in {
    test(new Thruwire(16)) { t =>

      t.io.in.poke(0.U)
      t.io.out.expect(0.U)

      t.io.in.poke(1024.U)
      t.io.out.expect(1024.U)
    }
  }
}
