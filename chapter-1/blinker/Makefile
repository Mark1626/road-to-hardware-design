.ONESHELL=
# .SHELLFLAGS=-e

IVERILOG = iverilog

bench/tblinker_bench: bench/tblinker_bench.v rtl/blinker.v
	$(IVERILOG) -o $@ $^

generate: rtl/blinker.v
	verilator -Wall -GCLOCK_RATE_HZ=10 --trace -cc $<
	cd obj_dir && make -f Vblinker.mk

bench/tblinker: bench/tblinker.cc generate
	g++ -o $@ $< \
	/usr/share/verilator/include/verilated.cpp \
	/usr/share/verilator/include/verilated_vcd_c.cpp \
	`pkg-config --libs --cflags verilator` \
	-I ./obj_dir ./obj_dir/Vblinker__ALL.a
