# clk => 100000000Hz
set_property PACKAGE_PIN P7 [get_ports { clock }]
create_clock -name clk -period 10.0 [get_ports { clk }]
set_property PACKAGE_PIN P8 [get_ports { reset }]

set_property PACKAGE_PIN J11 [get_ports { io_led0 }]
set_property PACKAGE_PIN K11 [get_ports { io_led1 }]
set_property PACKAGE_PIN K12 [get_ports { io_led2 }]
set_property PACKAGE_PIN K14 [get_ports { io_led3 }]

set_property PACKAGE_PIN L12 [get_ports { io_led4 }]
set_property PACKAGE_PIN L14 [get_ports { io_led5 }]
set_property PACKAGE_PIN M12 [get_ports { io_led6 }]
set_property PACKAGE_PIN N14 [get_ports { io_led7 }]

set_property PACKAGE_PIN P14 [get_ports { io_txd }]
set_property PACKAGE_PIN M9 [get_ports { io_rxd }]
