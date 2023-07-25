open_project -reset sum_prj

set_top sum

add_files sum.cc

open_solution "solution" -flow_target vivado

set_part {xc7a35ticsg324-1L}

create_clock -period 10 -name default
config_interface --m_axi_addr64=false

# Simulation
# csim_design

# Synth to Verilog/VHDL
csynth_design

# Cosim
# cosim_design -trace_level all -enable_dataflow_profiling

exit
