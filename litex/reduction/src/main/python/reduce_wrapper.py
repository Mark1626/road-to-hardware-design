from migen import *

from litex.soc.interconnect.csr import *
from litex.soc.interconnect import stream

from litex.soc.cores.dma import WishboneDMAReader

class Reduce(Module, AutoCSR):
    def __init__(self, bus):
        self.clock = ClockSignal()
        self.reset = Signal()
        self.bus = bus
        self.submodules.dma = WishboneDMAReader(bus, with_csr=True, endianness="big")

        self._result    = CSRStatus(description="Result", reset=0)
        self._busy      = CSRStatus(description="Module busy")
        self._fn_done   = CSRStatus(description="Reducer done", reset=0)

        self.start_port = Signal()
        self.return_port = Signal(32)

        self.started = Signal(reset=0)

        self.module_ready = Signal()

        self.comb += [
            self.reset.eq(self.dma._enable.re),
            self.start_port.eq(self.dma._enable.re),
            self.dma.source.ready.eq(self.module_ready),
            self._busy.status.eq(self.started)
        ]

        self.sync += [
            If(self.start_port & ~self.started, self.started.eq(1)),
        ]

        self.specials += Instance("reduce",
            i_clock=self.clock,
            i_reset=self.reset,
            i_start_port=self.start_port,
            i_arr_dout=self.dma.source.data,
            i_n=2,
            #i_arr_empty_n=self.dma.source.valid,
            i_arr_empty_n=self.started,
            o_done_port=self._fn_done.status,
            o_return_port=self._result.status,
            o_arr_read=self.module_ready)
