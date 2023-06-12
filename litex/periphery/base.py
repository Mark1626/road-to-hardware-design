#!/usr/bin/env python3

from migen import *

from litex.build.generic_platform import *
from litex.build.xilinx import XilinxPlatform

from litex.soc.integration.soc_core import *
from litex.soc.integration.builder import *
from litex.soc.cores import dna
from litex.soc.cores.uart import UARTWishboneBridge

from ios import Led, Button

# IOs ----------------------------------------------------------------------------------------------

_io = [
    ("user_led",  0, Pins("H5"), IOStandard("LVCMOS33")),
    ("user_sw",  0, Pins("A8"), IOStandard("LVCMOS33")),
    ("user_btn", 0, Pins("D9"), IOStandard("LVCMOS33")),
    ("clk100", 0, Pins("E3"), IOStandard("LVCMOS33")),
    ("cpu_reset", 0, Pins("C2"), IOStandard("LVCMOS33")),
    ("serial", 0,
        Subsignal("tx", Pins("D10")),
        Subsignal("rx", Pins("A9")),
        IOStandard("LVCMOS33"),
    )
]

# Platform -----------------------------------------------------------------------------------------

class Platform(XilinxPlatform):
    default_clk_name   = "clk100"
    default_clk_period = 1e9/100e6

    def __init__(self):
        XilinxPlatform.__init__(self, "xc7a35ticsg324-1L", _io, toolchain="vivado")

# Design -------------------------------------------------------------------------------------------

# Create our platform (fpga interface)
platform = Platform()

class BaseSoC(SoCMini):
    def __init__(self, platform, **kwargs):
        sys_clk_freq = int(100e6)

        SoCMini.__init__(self, platform, sys_clk_freq, csr_data_width=32,
            ident="SoC", ident_version=True)
        
        self.submodules.crg = CRG(platform.request("clk100"), ~platform.request("cpu_reset"))

        self.submodules.serial_bridge = UARTWishboneBridge(platform.request("serial"), sys_clk_freq)
        self.add_wb_master(self.serial_bridge.wishbone)

        self.submodules.dna = dna.DNA()
        self.add_csr("dna")

        user_leds = Cat(*[platform.request("user_led", i) for i in range(1)])
        self.submodules.leds = Led(user_leds)
        self.add_csr("leds")

        user_buttons = Cat(*[platform.request("user_btn", i) for i in range(1)])
        self.submodules.buttons = Button(user_buttons)
        self.add_csr("buttons")

soc = BaseSoC(platform)

builder = Builder(soc, output_dir="build", csr_csv="test/csr.csv")
builder.build(build_name="top")
