# Uart Print

Hello World with Bambu HLS based on this [repo](https://github.com/cfib/bf2hw.git)

## Build steps

```sh
export bambu=/path/to/bambu

# Generate Verilog files using Bambu HLS
make

fusesoc library add fusesoc_cores https://github.com/fusesoc/fusesoc-cores
fusesoc library add uart_print $PWD
fusesoc run --target=alchitry-cu mark:demo:print:0
```
