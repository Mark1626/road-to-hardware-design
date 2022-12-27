# Road to hardware design

In 2021 I started a repo to document my journey into HPC in [road-to-plus-plus](https://github.com/Mark1626/road-to-plus-plus). This repo is focussed on FPGA and hardware design.

## Chapters 

- [Chapter 1](./chapter-1/): FPGA Basic Experiments in Verilog


-------------------------------------------------------------------------------------------

## Hardware Design Languages

### Verilog

Open Source Verilog compiler https://github.com/steveicarus/iverilog

**Tutorials:**

1. http://asic-world.com/verilog/veritut.html
2. https://nandland.com/introduction-to-verilog-for-beginners-with-code-examples/
3. https://www.fpga4fun.com/
4. https://zipcpu.com/tutorial/
5. https://www.fpga4fun.com/HDLtutorials.html
6. https://verilogguide.readthedocs.io/en/latest/index.html

### Chisel

1. https://github.com/chipsalliance/chisel3

-------------------------------------------------------------------------------------------

## Simulators

1. Verilator https://www.veripool.org/verilator/#

-------------------------------------------------------------------------------------------

## FPGAs

### Xilinx FPGAs

- https://www.xilinx.com/products/silicon-devices/fpga.html

### Open FPGA

- https://makerchip.com/
- https://github.com/os-fpga/GettingStartedWithFPGAs
- https://fpgatutorial.com/open-source-fpga-tools/

Tiny FPGA https://tinyfpga.com/

### ICE40

- [Hackaday ICE40](https://hackaday.com/tag/ice40/)
- [Project Icestorm](https://clifford.at/icestorm)
- [Three part deep dive explains lattice ice40 fpga details](https://hackaday.com/2018/09/27/three-part-deep-dive-explains-lattice-ice40-fpga-details/)

-------------------------------------------------------------------------------------------

## Open Cores

- https://opencores.org/
- https://alchitry.com/projects/gpu
- https://alchitry.com/memory-mapping-verilog

-------------------------------------------------------------------------------------------

## Formal Verification

### SymbiYosys


-------------------------------------------------------------------------------------------

## Synthesis

- [yosys](https://yosyshq.net/yosys/)
- [abc](https://people.eecs.berkeley.edu/~alanmi/abc/)

--------------------------------------------------------------------------------------------

## RISC V

### Rocketchip

[RISC V Rocketchip Bootcamp](https://riscv.org/wp-content/uploads/2015/01/riscv-rocket-chip-tutorial-bootcamp-jan2015.pdf)
[Available Node Types in rocketchip](https://github.com/chipsalliance/rocket-chip/blob/master/src/main/scala/diplomacy/Nodes.scala)
[Diplomatic adder](https://github.com/chipsalliance/rocket-chip/blob/master/docs/src/diplomacy/adder_tutorial.md)


--------------------------------------------------------------------------------------------

## System on Chip

### Chipyard

- [Chipyard](https://fires.im/isca21-slides-pdf/02_chipyard_basics.pdf)
- [Chipyard adding an accelerator](https://chipyard.readthedocs.io/en/1.0.0/Customization/Adding-An-Accelerator.html)
- [Micro 2022 Chipyard tutorial](https://fires.im/micro-2022-tutorial/)

### Diplomacy

- [A Crash Course in the Diplomacy Framework](https://www.youtube.com/watch?v=4VfMCO4q26g)
- [Diplomacy and Tileset Reference](https://chipyard.readthedocs.io/en/latest/TileLink-Diplomacy-Reference/index.html)
- [Available Node Types in rocketchip](https://github.com/chipsalliance/rocket-chip/blob/master/src/main/scala/diplomacy/Nodes.scala)
- [Diplomatic adder](https://github.com/chipsalliance/rocket-chip/blob/master/docs/src/diplomacy/adder_tutorial.md)

## Accelerators



### AMBA

--------------------------------------------------------------------------------------------

## Open Source Toolchain

- [F4PGA](https://github.com/chipsalliance/f4pga)
- [APIO](https://github.com/FPGAwars/apio)
- [Icestudio](https://github.com/FPGAwars/icestudio)

--------------------------------------------------------------------------------------------

## LLVM CIRCT

- [LLVM Circt](https://github.com/llvm/circt)
- [Sifive chisel to circt](https://github.com/sifive/chisel-circt)
  + Adds a Chisel stage that generates LLVM circt

--------------------------------------------------------------------------------------------

## Papers

- [Running Identical Threads in C-Slow Retiming based Designs for Functional Failure Detection](https://arxiv.org/pdf/1502.01237.pdf)
- [Time and area efficient pattern matching on FPGAs](https://dl.acm.org/doi/10.1145/968280.968312)
- [A Configurable Hardware Fault Injection Framework for RISC-V Systems](https://carrv.github.io/2018/papers/CARRV_2018_paper_2.pdf)


-------------------------------------------------------------------------------------------

## Platforms

1. Vitis https://www.xilinx.com/products/design-tools/vitis/vitis-platform.html
2. Vivado https://www.xilinx.com/products/design-tools/vivado.html
3. Digilent Adept https://digilent.com/shop/software/digilent-adept/
  1. This is available in Raspberry PI

-------------------------------------------------------------------------------------------

## Videos

- [Rocket chip ecosystem](https://www.youtube.com/watch?v=Eko86PGEoDY)
- [Rapid Accelerator Design with Chisel](https://www.youtube.com/watch?v=IZeUHzukStE)

## Blogs and Reading

1. http://zipcpu.com/
2. https://lowrisc.org/

- [Chiffre - Hardware Fault Injection Framework](https://carrv.github.io/2018/papers/CARRV_2018_paper_2.pdf)
- [Hwacha Vector-Fetch Architecture Manual](https://www2.eecs.berkeley.edu/Pubs/TechRpts/2015/EECS-2015-262.pdf)
- [The Rocket Chip Generator](https://www2.eecs.berkeley.edu/Pubs/TechRpts/2016/EECS-2016-17.pdf)
- [Untethering the RISC-V Rocket Chip](https://riscv.org/wp-content/uploads/2016/01/Wed1115-untether_wsong83.pdf)
- [Chipyard prototyping for Arty and VCU118](https://chipyard.readthedocs.io/en/stable/Prototyping/Arty.html)

## Books

- [Chisel Book](http://www.imm.dtu.dk/~masca/chisel-book.pdf)


