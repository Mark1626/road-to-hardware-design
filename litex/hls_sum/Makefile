all: run

#  --bus-standard axi-lite

pre-compile:
	python base.py --integrated-main-ram-size=0x10000 --cpu-type=vexriscv \
		--no-compile-gateware \
		--cpu-variant=standard+debug --sim-debug --trace --gtkwave-savefile

compile:
	make -C demo-sim

run: compile
	python base.py --integrated-main-ram-size=0x10000 --cpu-type=vexriscv \
		--ram-init=demo-sim/demo.bin \
		--cpu-variant=standard+debug --sim-debug --trace --gtkwave-savefile

.PHONY: pre-compile compile run
