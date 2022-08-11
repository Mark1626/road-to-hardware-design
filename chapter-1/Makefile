IVERILOG=iverilog

all: hello/hello \
			counter/tcounter \
			arbiter/tarbiter \
			adder/tadder

counter/tcounter: counter/tcounter.v counter/counter.v
	$(IVERILOG) -o $@ $^

arbiter/tarbiter: arbiter/tarbiter.v arbiter/arbiter.v
	$(IVERILOG) -o $@ $^

adder/bench/tadder: adder/bench/tadder.v \
	adder/rtl/full_adder.v \
	adder/rtl/half_adder.v \
	adder/rtl/ripple_adder_4bit.v \
	adder/rtl/ripple_adder_8bit.v 
	$(IVERILOG) -o $@ $^

clean:
	rm -f hello/hello \
			counter/tcounter \
			arbiter/tarbiter

.PHONY: clean all
