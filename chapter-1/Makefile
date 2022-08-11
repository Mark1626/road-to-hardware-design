IVERILOG=iverilog

all: hello/hello\
			counter/tcounter \
			arbiter/tarbiter

counter/tcounter: counter/tcounter.v counter/counter.v
	$(IVERILOG) -o $@ $^

arbiter/tarbiter: arbiter/tarbiter.v arbiter/arbiter.v
	$(IVERILOG) -o $@ $^

clean:
	rm -f hello/hello \
			counter/tcounter \
			arbiter/tarbiter

.PHONY: clean all
