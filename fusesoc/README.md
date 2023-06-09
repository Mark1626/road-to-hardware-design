# Fusesoc based projects

## Examples

1. Blinky: Simple blinky that works on Alchitry Cu and Arty A7 35T
2. Uart: UART echo example, that also prints the ascii of the character received on LEDs
3. PLL Blinky: PLL based blinky that uses the internal PLL in the ICE40 FPGA to reach desired frequency (Works only on ICE 40 based boards)

## Building and running

To setup the project

```sh
cd <example>
fusesoc library add fusesoc_cores https://github.com/fusesoc/fusesoc-cores
fusesoc library add <example> $PWD
fusesoc run --target=alchitry-cu <example>
```

