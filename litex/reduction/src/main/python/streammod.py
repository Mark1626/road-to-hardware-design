from migen import *

from litex.soc.interconnect.csr import *
from litex.soc.interconnect import stream

from litex.soc.cores.dma import WishboneDMAReader, WishboneDMAWriter

class StreamModule(Module, AutoCSR):
    def __init__(self, bus_read, bus_write):
        self.clock = ClockSignal()
        self.reset = Signal()
        self.submodules.dmaread  = WishboneDMAReader(bus_read, with_csr=True)
        self.submodules.dmawrite = WishboneDMAWriter(bus_write)
        self.dmawrite.add_csr(ready_on_idle=False)

        self.submodules.sync_fifo = sync_fifo = stream.SyncFIFO([("data", 32)], 100)
        self.comb += [
            self.dmaread.source.connect(sync_fifo.sink),
            sync_fifo.source.connect(self.dmawrite.sink)
        ]
