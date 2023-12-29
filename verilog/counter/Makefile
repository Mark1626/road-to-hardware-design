tcounter: tcounter.v counter.v
	iverilog -o tcounter tcounter.v counter.v

run-tb: tcounter
	vvp tcounter

check.smt2: counter.v
	yosys -v2 -p 'read_verilog -formal counter.v' \
					 -p 'prep -top counter -nordff' \
					 -p 'assertpmux -noinit; opt -fast; async2sync; dffunmap' \
					 -p 'write_smt2 -wires check.smt2'

verify: check.smt2
	yosys-smtbmc -s z3 -t 30 --dump-vcd check.vcd check.smt2
	yosys-smtbmc -s z3 -t 30 --dump-vcd check.vcd -i check.smt2

.PHONY: run-tb verify
