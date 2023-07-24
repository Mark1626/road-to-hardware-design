from migen import *

from litex.soc.integration.common import *
from litex.soc.integration.soc_core import *
from litex.soc.integration.builder import *
from litex.soc.integration.soc import *

from litex.soc.interconnect import axi, wishbone

class Fact(Module, AutoCSR):
    def __init__(self):
        self.clock = ClockSignal()
        self.reset = ResetSignal()

        self._x = CSRStorage(32)
        self._y = CSRStatus(32)
        self._done = CSRStatus(reset=0)

        self.yval_sig = Signal(32)
        self.done_sig = Signal()

        self.sync += [
            If (self.done_sig, self._y.status.eq(self.yval_sig)),
            If (self.done_sig, self._done.status.eq(self.done_sig)),
        ]

        self.specials += Instance("Factorial",
            i_clock = self.clock,
            i_reset = self.reset,

            i_io_in_bits = self._x.storage,
            i_io_in_valid = self._x.re,

            o_io_out_bits = self.yval_sig,
            o_io_out_valid = self.done_sig,
            i_io_out_ready = 1
        )

class FactHLS(Module, AutoCSR):
    def __init__(self):
        self.clock = ClockSignal()
        self.reset = ResetSignal()

        self._x = CSRStorage(32)
        self._y = CSRStatus(32)
        self._done = CSRStatus(reset=0)

        self.started = Signal(reset=0)
        self.trn_done = Signal(reset=0)

        self.yval_sig = Signal(32)
        self.done_sig = Signal()

        self.sync += [
            If(self._x.re, self.started.eq(self._x.re)),
            If (self.trn_done, self.started.eq(0)),
            If (self.done_sig, self._done.status.eq(self.done_sig)),
            If (self.done_sig, self._y.status.eq(self.yval_sig)),
        ]

        self.specials += Instance("fact",
            i_ap_clk = self.clock,
            i_ap_rst = self.reset,

            i_x = self._x.storage,
            i_ap_start = self.started,
            i_ap_ready = self.trn_done,

            o_ap_return = self.yval_sig,
            o_ap_done = self.done_sig
            )

class FactHLSAXI(Module, AutoCSR):
    def __init__(self, bus=None):
        # self.bus   = bus
        self.s_axi = axi.AXILiteInterface(address_width=32, data_width=32)

        # wb2axi = axi.Wishbone2AXILite(self.bus, self.s_axi)

        # self.submodules += wb2axi

        self.ap_clk = ClockSignal()
        self.ap_rst_n = ResetSignal()
        self.interrupt = Signal()

        # Instance of the Verilog module
        self.specials += Instance("fact",
            # Clock and Reset
            i_ap_clk=self.ap_clk,
            i_ap_rst_n=~self.ap_rst_n,

            # AR
            i_s_axi_control_ARVALID=self.s_axi.ar.valid,
            o_s_axi_control_ARREADY=self.s_axi.ar.ready,
            i_s_axi_control_ARADDR=self.s_axi.ar.addr,

            # R
            o_s_axi_control_RVALID=self.s_axi.r.valid,
            i_s_axi_control_RREADY=self.s_axi.r.ready,
            o_s_axi_control_RDATA=self.s_axi.r.data,
            o_s_axi_control_RRESP=self.s_axi.r.resp,

            # AW
            i_s_axi_control_AWVALID=self.s_axi.aw.valid,
            o_s_axi_control_AWREADY=self.s_axi.aw.ready,
            i_s_axi_control_AWADDR=self.s_axi.aw.addr,

            # W
            i_s_axi_control_WVALID=self.s_axi.w.valid,
            o_s_axi_control_WREADY=self.s_axi.w.ready,
            i_s_axi_control_WDATA=self.s_axi.w.data,
            i_s_axi_control_WSTRB=self.s_axi.w.strb,

            # B
            o_s_axi_control_BVALID=self.s_axi.b.valid,
            i_s_axi_control_BREADY=self.s_axi.b.ready,
            o_s_axi_control_BRESP=self.s_axi.b.resp,

            # Interrupt
            o_interrupt=self.interrupt
        )
