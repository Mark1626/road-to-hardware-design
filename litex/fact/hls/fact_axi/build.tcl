open_project -reset fact_prj

set_top fact

add_files fact.cc

open_solution "solution" -flow_target vivado

set_part {xc7a35ticsg324-1L}

create_clock -period 10 -name default

# Simulation
# csim_design

# Synth to Verilog/VHDL
csynth_design

# Cosim
# cosim_design -trace_level all -enable_dataflow_profiling

exit
