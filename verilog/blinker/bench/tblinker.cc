#include <cstdio>

#include "Vblinker.h"
#include "verilated.h"
#include "verilated_vcd_c.h"

void tick(int tickcount, Vblinker *tb, VerilatedVcdC *tfp) {
  tb->eval();
  if (tfp)
    tfp->dump(tickcount * 10 - 2);

  tb->i_clk = 1;
  tb->eval();
  if (tfp)
    tfp->dump(tickcount * 10);

  tb->i_clk = 0;
  tb->eval();
  if (tfp) {
    tfp->dump(tickcount * 10 + 5);
    tfp->flush();
  }
}

int main(int argc, char **argv) {
  Verilated::traceEverOn(true);
  VerilatedVcdC *tfp = new VerilatedVcdC();

  Vblinker *tb = new Vblinker();

  tb->trace(tfp, 99);
  tfp->open("blinker.vcd");

  unsigned tickcount = 0;

  int last_led = tb->o_out;
  for (int k = 0; k < (1 << 20); k++) {
    tick(++tickcount, tb, tfp);
  }

  // delete [] tfp;
  // delete [] tb;
}
