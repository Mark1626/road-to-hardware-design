# Clock pin
set_property -dict { PACKAGE_PIN E3 IOSTANDARD LVCMOS33 } [get_ports {clock}]

# Switches
set_property -dict { PACKAGE_PIN C2 IOSTANDARD LVCMOS33 } [get_ports { reset }]

# LEDs
set_property -dict { PACKAGE_PIN H5 IOSTANDARD LVCMOS33 } [get_ports {io_led0}]
set_property -dict { PACKAGE_PIN J5 IOSTANDARD LVCMOS33 } [get_ports {io_led1}]
set_property -dict { PACKAGE_PIN T9 IOSTANDARD LVCMOS33 } [get_ports {io_led2}]
set_property -dict { PACKAGE_PIN T10 IOSTANDARD LVCMOS33 } [get_ports { io_led3 }]; #IO_L24N_T3_A00_D16_14 Sch=led[7]

set_property -dict { PACKAGE_PIN E1    IOSTANDARD LVCMOS33 } [get_ports { io_led4 }]; #IO_L18N_T2_35 Sch=led0_b
# set_property -dict { PACKAGE_PIN F6    IOSTANDARD LVCMOS33 } [get_ports { RGB0_Green }]; #IO_L19N_T3_VREF_35 Sch=led0_g
# set_property -dict { PACKAGE_PIN G6    IOSTANDARD LVCMOS33 } [get_ports { RGB0_Red }]; #IO_L19P_T3_35 Sch=led0_r
set_property -dict { PACKAGE_PIN G4    IOSTANDARD LVCMOS33 } [get_ports { io_led5 }]; #IO_L20P_T3_35 Sch=led1_b
# set_property -dict { PACKAGE_PIN J4    IOSTANDARD LVCMOS33 } [get_ports { RGB1_Green }]; #IO_L21P_T3_DQS_35 Sch=led1_g
# set_property -dict { PACKAGE_PIN G3    IOSTANDARD LVCMOS33 } [get_ports { RGB1_Red }]; #IO_L20N_T3_35 Sch=led1_r
set_property -dict { PACKAGE_PIN H4    IOSTANDARD LVCMOS33 } [get_ports { io_led6 }]; #IO_L21N_T3_DQS_35 Sch=led2_b
# set_property -dict { PACKAGE_PIN J2    IOSTANDARD LVCMOS33 } [get_ports { RGB2_Green }]; #IO_L22N_T3_35 Sch=led2_g
# set_property -dict { PACKAGE_PIN J3    IOSTANDARD LVCMOS33 } [get_ports { RGB2_Red }]; #IO_L22P_T3_35 Sch=led2_r
set_property -dict { PACKAGE_PIN K2    IOSTANDARD LVCMOS33 } [get_ports { io_led7 }]; #IO_L23P_T3_35 Sch=led3_b
# set_property -dict { PACKAGE_PIN H6    IOSTANDARD LVCMOS33 } [get_ports { RGB3_Green }]; #IO_L24P_T3_35 Sch=led3_g
# set_property -dict { PACKAGE_PIN K1    IOSTANDARD LVCMOS33 } [get_ports { RGB3_Red }]; #IO_L23N_T3_35 Sch=led3_r

#USB-UART Interface

set_property -dict { PACKAGE_PIN D10   IOSTANDARD LVCMOS33 } [get_ports { io_txd }]; #IO_L19N_T3_VREF_16 Sch=uart_rxd_out
set_property -dict { PACKAGE_PIN A9    IOSTANDARD LVCMOS33 } [get_ports { io_rxd }]; #IO_L14N_T2_SRCC_16 Sch=uart_txd_in



# Clock constraints
create_clock -period 10.0 [get_ports {clock}]
