#!/usr/bin/env python3

import sys

from migen import *

from litex_boards.targets import digilent_arty

from litex.soc.integration.common import *
from litex.soc.integration.soc_core import *
from litex.soc.integration.builder import *
from litex.soc.integration.soc import *

from litex.tools import litex_sim

def extend_target(target):
    """
    :param: target: Refers to a target from litex_boards
    """
    def custom_target(customize_fn):
        target(customize_fn)

    return custom_target

@extend_target(litex_sim.main)
# @extend_target(digilent_arty.main)
def MasterTarget(soc):
    from sum import SumHLSAXI
    soc.submodules.sum = SumHLSAXI()
    soc.bus.add_slave("s_sumhls", soc.sum.s_axilite, SoCRegion(
        origin=0x2000_0000,
        size=0x100
    ))
    soc.bus.add_master("m_sumhls", soc.sum.m_axi)
    soc.platform.add_source_dir("./hls/sum/sum_prj/solution/impl/verilog")
