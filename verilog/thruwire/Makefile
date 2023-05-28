
generate: rtl/thruwire.v
	verilator -Wall -cc $<
	cd obj_dir
	make -f Vthruwire.mk

bench/tthruwire: bench/tthruwire.cc generate
	g++ -o $@ $< /usr/share/verilator/include/verilated.cpp \
		`pkg-config --libs --cflags verilator` \
		-I ./obj_dir ./obj_dir/Vthruwire__ALL.a
