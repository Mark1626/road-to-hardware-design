# Chapter 3 - ICE40


## OpenSource Tools

1. iverilog/Chisel - HDL
2. yosys - Synthesis
3. nextpnr - Place and Route
4. icepack - Package bitstream
5. iceprog - Upload bitstream

## Examples

1. UartEcho: A simple Uart interface that send data received in rx to tx. This uses a Chisel blackbox around FPGAWars UART Peripheral IP
    a. Uses 2 stop signals
    b. Testing in Alchitry Cu ICE40
2. UartMemory: Stores data from Uart into block ram, reads it back and sends it over tx
    a. This is not working on physical hardware, suspect a timing issue
    b. Tests and examining VCD indicate this is working 

