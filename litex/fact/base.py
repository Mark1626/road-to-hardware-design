#!/usr/bin/env python3

import sys

from migen import *

from litex_boards.targets import digilent_arty

from litex.soc.integration.common import *
from litex.soc.integration.soc_core import *
from litex.soc.integration.builder import *
from litex.soc.integration.soc import *

from litex.tools import litex_sim

from fact import Fact, FactHLS, FactHLSAXI

def extend_target(target):
    """
    :param: target: Refers to a target from litex_boards
    """
    def custom_target(customize_fn):
        target(customize_fn)

    return custom_target

class Box(Module, AutoCSR):
    def __init__(self):
        self.clock = ClockSignal()
        self.reset = ResetSignal()

        self._x = CSRStorage(32)
        self._y = CSRStatus(32)

        self.specials += Instance("Box",
            i_clock = self.clock,
            i_reset = self.reset,
            i_x = self._x.storage,
            o_y = self._y.status
        )


@extend_target(litex_sim.main)
# @extend_target(digilent_arty.main)
def MyTarget(soc):
    # soc.submodules.box = Box()
    # soc.platform.add_source("./box.v")

    soc.submodules.fact = Fact()
    soc.platform.add_source("./fact.v")

    # soc.submodules.facthls = FactHLS()
    # soc.platform.add_source("./hls/fact_1/fact_prj/solution/impl/verilog/fact.v")
    # soc.platform.add_source("./hls/fact_1/fact_prj/solution/impl/verilog/fact_mul_31ns_32s_32_2_1.v")
    # soc.platform.add_source("./hls/fact_1/fact_prj/solution/impl/verilog/fact_flow_control_loop_pipe.v")

    # factwb = wishbone.Interface(
    #     data_width=soc.bus.data_width,
    #     adr_width=soc.bus.get_address_width(standard="axi-lite")
    # )
    soc.submodules.factaxi = FactHLSAXI()
    soc.bus.add_slave("factwb", soc.factaxi.s_axi, SoCRegion(
        origin = 0x2000_0000,
        size = 0x100
    ))
    soc.platform.add_source("./hls/fact_axi/fact_prj/solution/impl/verilog/fact.v")
    soc.platform.add_source("./hls/fact_axi/fact_prj/solution/impl/verilog/fact_control_s_axi.v")
    soc.platform.add_source("./hls/fact_axi/fact_prj/solution/impl/verilog/fact_mul_31ns_32s_32_2_1.v")
