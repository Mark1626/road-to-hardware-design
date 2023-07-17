from migen import *

from litex.soc.interconnect.csr import *
from litex.soc.interconnect import stream

from litex.soc.cores.dma import WishboneDMAReader

class StreamSum(Module, AutoCSR):
  def __init__(self, bus):
    self.bus = bus
    self._sum = CSRStatus(32, description="Sum", reset=0)
    self._sum_done = CSRStatus(description="Sum done", reset=0)

    self.submodules.dma = WishboneDMAReader(bus, with_csr=True, endianness="big")

    self.sync += [
      self.dma.source.ready.eq(1),
      If(self.dma.source.valid & self.dma.source.ready,
        self._sum.status.eq(self._sum.status + self.dma.source.data)
      ),
      self._sum_done.status.eq(self.dma.source.last)
    ]
