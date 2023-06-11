io=$(PWD)/src/main/c/lib/bambu_io_hw

main:
	$(bambu) -O3 -v4 --std=c99 src/main/c/main.c --reset-type=async --reset-level=high \
    --clock-period=41.67 $(io)/constraints_STD.xml $(io)/BF2HW_IPs.xml \
    --file-input-data=$(io)/bambu_getchar.v,$(io)/bambu_putchar.v --experimental-setup=BAMBU
	mv *.v src/main/resources/
	mv *.mem src/main/resources/
