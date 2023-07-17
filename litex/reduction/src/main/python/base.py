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
def MyTarget(soc):
    # from streamsum import StreamSum
    # bus = wishbone.Interface(data_width=soc.bus.data_width, adr_width=soc.bus.get_address_width(standard="wishbone"))

    # soc.submodules.ssum = StreamSum(bus)
    # soc.bus.add_master("ssum", master=bus)

    # from reduce_wrapper import Reduce

    # bus = wishbone.Interface(data_width=soc.bus.data_width, adr_width=soc.bus.get_address_width(standard="wishbone"))
    # soc.submodules.hlssum = Reduce(bus)
    # soc.bus.add_master("hlssum", master=bus)

    from streammod import StreamModule
    stream_read = wishbone.Interface(data_width=soc.bus.data_width, adr_width=soc.bus.get_address_width(standard="wishbone"))
    stream_write = wishbone.Interface(data_width=soc.bus.data_width, adr_width=soc.bus.get_address_width(standard="wishbone"))

    soc.submodules.smod = StreamModule(stream_read, stream_write)
    soc.bus.add_master("stream_read", master=stream_read)
    soc.bus.add_master("stream_write", master=stream_write)

    soc.platform.add_source("./src/main/hls/reduce.v")
