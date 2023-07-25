from migen import *

from litex.soc.integration.common import *
from litex.soc.integration.soc_core import *
from litex.soc.integration.builder import *
from litex.soc.integration.soc import *

from litex.soc.interconnect import axi

class SumHLSAXI(Module, AutoCSR):
    def __init__(self):
        self.clock = ClockSignal()
        self.reset = ResetSignal()

        self.s_axilite  = axi.AXILiteInterface(address_width=32, data_width=32)
        self.m_axi      = axi.AXIInterface(
            address_width=32,
            data_width=32,
            id_width=1,
            aw_user_width=1,
            w_user_width=1,
            b_user_width=1,
            ar_user_width=1,
            r_user_width=1)

        self.interrupt = Signal()

        self.specials += Instance("sum",
            # Clock and Reset
            i_ap_clk=self.clock,
            i_ap_rst_n=~self.reset,

            # AXI Master Interface to memory
            o_m_axi_gmem0_AWVALID=self.m_axi.aw.valid,
            i_m_axi_gmem0_AWREADY=self.m_axi.aw.ready,
            o_m_axi_gmem0_AWADDR=self.m_axi.aw.addr,
            o_m_axi_gmem0_AWID=self.m_axi.aw.id,
            o_m_axi_gmem0_AWLEN=self.m_axi.aw.len,
            o_m_axi_gmem0_AWSIZE=self.m_axi.aw.size,
            o_m_axi_gmem0_AWBURST=self.m_axi.aw.burst,
            o_m_axi_gmem0_AWLOCK=self.m_axi.aw.lock,
            o_m_axi_gmem0_AWCACHE=self.m_axi.aw.cache,
            o_m_axi_gmem0_AWPROT=self.m_axi.aw.prot,
            o_m_axi_gmem0_AWQOS=self.m_axi.aw.qos,
            o_m_axi_gmem0_AWREGION=self.m_axi.aw.region,
            o_m_axi_gmem0_AWUSER=self.m_axi.aw.user,

            o_m_axi_gmem0_WVALID=self.m_axi.w.valid,
            i_m_axi_gmem0_WREADY=self.m_axi.w.ready,
            o_m_axi_gmem0_WDATA=self.m_axi.w.data,
            o_m_axi_gmem0_WSTRB=self.m_axi.w.strb,
            o_m_axi_gmem0_WLAST=self.m_axi.w.last,
            o_m_axi_gmem0_WID=self.m_axi.w.id,
            o_m_axi_gmem0_WUSER=self.m_axi.w.user,

            o_m_axi_gmem0_ARVALID=self.m_axi.ar.valid,
            i_m_axi_gmem0_ARREADY=self.m_axi.ar.ready,
            o_m_axi_gmem0_ARADDR=self.m_axi.ar.addr,
            o_m_axi_gmem0_ARID=self.m_axi.ar.id,
            o_m_axi_gmem0_ARLEN=self.m_axi.ar.len,
            o_m_axi_gmem0_ARSIZE=self.m_axi.ar.size,
            o_m_axi_gmem0_ARBURST=self.m_axi.ar.burst,
            o_m_axi_gmem0_ARLOCK=self.m_axi.ar.lock,
            o_m_axi_gmem0_ARCACHE=self.m_axi.ar.cache,
            o_m_axi_gmem0_ARPROT=self.m_axi.ar.prot,
            o_m_axi_gmem0_ARQOS=self.m_axi.ar.qos,
            o_m_axi_gmem0_ARREGION=self.m_axi.ar.region,
            o_m_axi_gmem0_ARUSER=self.m_axi.ar.user,

            # AXI Master Read Responses
            i_m_axi_gmem0_RVALID=self.m_axi.r.valid,
            o_m_axi_gmem0_RREADY=self.m_axi.r.ready,
            i_m_axi_gmem0_RDATA=self.m_axi.r.data,
            i_m_axi_gmem0_RLAST=self.m_axi.r.last,
            i_m_axi_gmem0_RID=self.m_axi.r.id,
            i_m_axi_gmem0_RUSER=self.m_axi.r.user,
            i_m_axi_gmem0_RRESP=self.m_axi.r.resp,

            # AXI Master Write Responses
            i_m_axi_gmem0_BVALID=self.m_axi.b.valid,
            o_m_axi_gmem0_BREADY=self.m_axi.b.ready,
            i_m_axi_gmem0_BRESP=self.m_axi.b.resp,
            i_m_axi_gmem0_BID=self.m_axi.b.id,
            i_m_axi_gmem0_BUSER=self.m_axi.b.user,

            # Control AXILite

            # AR
            i_s_axi_control_ARVALID=self.s_axilite.ar.valid,
            o_s_axi_control_ARREADY=self.s_axilite.ar.ready,
            i_s_axi_control_ARADDR=self.s_axilite.ar.addr,

            # R
            o_s_axi_control_RVALID=self.s_axilite.r.valid,
            i_s_axi_control_RREADY=self.s_axilite.r.ready,
            o_s_axi_control_RDATA=self.s_axilite.r.data,
            o_s_axi_control_RRESP=self.s_axilite.r.resp,

            # AW
            i_s_axi_control_AWVALID=self.s_axilite.aw.valid,
            o_s_axi_control_AWREADY=self.s_axilite.aw.ready,
            i_s_axi_control_AWADDR=self.s_axilite.aw.addr,

            # W
            i_s_axi_control_WVALID=self.s_axilite.w.valid,
            o_s_axi_control_WREADY=self.s_axilite.w.ready,
            i_s_axi_control_WDATA=self.s_axilite.w.data,
            i_s_axi_control_WSTRB=self.s_axilite.w.strb,

            # B
            o_s_axi_control_BVALID=self.s_axilite.b.valid,
            i_s_axi_control_BREADY=self.s_axilite.b.ready,
            o_s_axi_control_BRESP=self.s_axilite.b.resp,

            # Interrupt
            o_interrupt=self.interrupt
        )
