%: %.cc
	$(BAMBU) $< -O2 --top-fname=$@ --device-name=xc7a100t-1csg324-VVD \
		--clock-period=10 --generate-interface=INFER --compiler=I386_CLANG13 \
		--generate-vcd --generate-tb=$@.xml --simulator=VERILATOR

clean:
	rm -rf *.v HLS_output *.sh

