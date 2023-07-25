open_project -reset test_hls_stream_prj

set_top RepeaterTop

add_files repeater.cc
add_files -tb repeater_test.cc

open_solution "solution" -flow_target vivado

set_part {xc7a35ticsg324-1L}

create_clock -period 10 -name default
config_interface --m_axi_addr64=false

# Simulation
csim_design

# Synth to Verilog/VHDL
csynth_design

# Cosim
# cosim_design -trace_level all -enable_dataflow_profiling

exit
