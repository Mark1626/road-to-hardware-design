.ONESHELL=
# .SHELLFLAGS=-e

IVERILOG = iverilog

bench/tshiftreg: bench/tshiftreg.v rtl/shiftreg.v
	$(IVERILOG) -o $@ $^

