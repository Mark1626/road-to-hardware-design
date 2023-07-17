#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <irq.h>
#include <uart.h>
#include <console.h>
#include <generated/mem.h>
#include <generated/csr.h>

static char *readstr(void)
{
	char c[2];
	static char s[64];
	static int ptr = 0;

	if(readchar_nonblock()) {
		c[0] = readchar();
		c[1] = 0;
		switch(c[0]) {
			case 0x7f:
			case 0x08:
				if(ptr > 0) {
					ptr--;
					putsnonl("\x08 \x08");
				}
				break;
			case 0x07:
				break;
			case '\r':
			case '\n':
				s[ptr] = 0x00;
				putsnonl("\n");
				ptr = 0;
				return s;
			default:
				if(ptr >= (sizeof(s) - 1))
					break;
				putsnonl(c);
				s[ptr] = c[0];
				ptr++;
				break;
		}
	}

	return NULL;
}

static char *get_token(char **str)
{
	char *c, *d;

	c = (char *)strchr(*str, ' ');
	if(c == NULL) {
		d = *str;
		*str = *str+strlen(*str);
		return d;
	}
	*c = 0;
	d = *str;
	*str = c+1;
	return d;
}

static void prompt(void)
{
	printf("RUNTIME>");
}

static void help(void)
{
	puts("Available commands:");
	puts("help                            - this command");
	puts("store_xy                        - store x y");
	puts("read_gcd                        - read GCD");
	puts("read_done                       - read done");
	puts("read_busy                       - read busy");
	puts("read_x_valid                    - read x valid");
	puts("read_y_valid                    - read y valid");
	puts("reboot                          - reboot");
}

static void reboot(void)
{
	ctrl_reset_write(1);
}

// static void test_ssum(void)
// {
// 	ssum_dma_base_write(SRAM_BASE + (0x1000));
// 	ssum_dma_length_write(32);
// 	ssum_dma_enable_write(1);

// 	printf("DMA Done %d\n", ssum_sum_done_read());
// 	printf("Val %d\n", ssum_sum_read());
// }

// static void test_hlssum(void)
// {
// 	hlssum_dma_base_write(SRAM_BASE + (0x1000));
// 	hlssum_dma_length_write(128);
// 	hlssum_dma_enable_write(1);

// 	printf("DMA Done %d\n", hlssum_fn_done_read());
// 	printf("Val %d\n", hlssum_result_read());
// }

static void test_ssmod(void)
{
	smod_dmaread_base_write(SRAM_BASE + (0x1000));
	smod_dmaread_length_write(16);
	smod_dmaread_enable_write(1);

	while(!smod_dmaread_done_read());

	busy_wait(1000);

	smod_dmawrite_base_write(SRAM_BASE + (0x1200));
	smod_dmawrite_length_write(16);
	smod_dmawrite_enable_write(1);

	while(!smod_dmawrite_done_read());

	busy_wait(1000);

	void *rom = (volatile void *)(SRAM_BASE + (0x1200));

	for (int i = 0; i < 8; i++) {
		printf("Val: %x %d %d\n", rom, i, *(uint32_t*)rom);
		rom += 4;
	}

}

int main(void)
{
#ifdef CONFIG_CPU_HAS_INTERRUPT
	irq_setmask(0);
	irq_setie(1);
#endif
	uart_init();

	// if (spisdcard_init())
	// 	printf("Successful.\n");
	// else
	// 	printf("Failed.\n");

	puts("\nStream test "__DATE__" "__TIME__"\n");

	void *rom = (volatile void *)(SRAM_BASE + (0x1000));

	for (int i = 0; i < 12; i++) {
		*(uint32_t*)rom = i+1;
		rom += 4;
	}

	sim_trace_enable_write(1);

	printf("TEst\n");

	// test_fs();

	// test_ssum();

	// test_hlssum();

	test_ssmod();

	sim_trace_enable_write(0);
	sim_finish_finish_write(1);

	return 0;
}
