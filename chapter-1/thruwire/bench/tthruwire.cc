#include <iostream>
#include "verilated.h"
#include "Vthruwire.h"

int main(int argc, char **argv) {
  Verilated::commandArgs(argc, argv);
  Vthruwire *tb = new Vthruwire();

  for (int k = 0; k < 20; k++) {
    tb->i_in = k & 1;
    tb->eval();

    std::cout << "k = " << k << std::endl;
    std::cout << "sw = " << (int)tb->i_in << std::endl;
    std::cout << "led = " << (int)tb->o_out << std::endl;
  }

}
