# Chapter 1: FPGA Basic Experiments in Verilog

1. **hello** Basic hello `iverilog`
2. **counter** 8 bit counter
3. **arbiter** Aribiter

## Tools

### iverilog

https://steveicarus.github.io/iverilog/index.html

`iverilog` is a verilog compiler. It generates code that can be used by backend tools

`vvp` is a simulation runtime engine 

### gtkwave

`gtkwave` is a wave viewer based on GTK+, Verilog VCD files are loaded in `gtkwave` to analyse the waveform of the design

-----------------------------------------------------------------------------

## Verilog

1. Difference between `assign`, `<=` and `=`

   - `<=` is non blocking and is performed on pos edge
   - `assign =` continual assignment to wire, it has to be outside `always`
   - `=` is blocking assignment, inside `always`

-----------------------------------------------------------------------------

## Other tips

> **Timing Diagram in MarkDown:**  
  Inline plantuml timing diagram can be created with  
  \<\!-- @startuml timing-diagram.png --\>  
  And the timing diagram can be created with plantuml \<file\>.md




