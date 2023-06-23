#!/usr/bin/env python3

from migen import *

from litex.build.generic_platform import *
from litex_boards.platforms import digilent_arty

# class TestHarness(Module):
#     def __init__(self, platform):
#         self.clock = ClockSignal()
#         self.reset = platform.request("cpu_reset")

#         self.io_led0 = platform.request("user_led", 0)
#         self.io_led1 = platform.request("user_led", 1)
#         self.io_led2 = platform.request("user_led", 2)
#         self.io_led3 = platform.request("user_led", 3)

#         self.specials += Instance("TestHarness",
#                                            i_clock=self.clock,
#                                            i_reset=self.reset,
#                                            o_io_led0=self.io_led0,
#                                            o_io_led1=self.io_led1,
#                                            o_io_led2=self.io_led2,
#                                            o_io_led3=self.io_led3)

class TestHarness(Module):
    def __init__(self, width):
        self.clock = ClockSignal()
        self.reset = platform.request("cpu_reset")

        self.serial = platform.request("serial")

        self.io_txd = self.serial.tx
        self.io_rxd = self.serial.rx
        self.io_led0 = platform.request("user_led", 0)
        self.io_led1 = platform.request("user_led", 1)
        self.io_led2 = platform.request("user_led", 2)
        self.io_led3 = platform.request("user_led", 3)
        self.io_led4 = platform.request("rgb_led", 0).r
        self.io_led5 = platform.request("rgb_led", 1).r
        self.io_led6 = platform.request("rgb_led", 2).r
        self.io_led7 = platform.request("rgb_led", 3).r

        self.specials += Instance("TestHarness",
                i_clock=self.clock,
                i_reset=self.reset,
                o_io_txd=self.io_txd,
                i_io_rxd=self.io_rxd,
                o_io_led0=self.io_led0,
                o_io_led1=self.io_led1,
                o_io_led2=self.io_led2,
                o_io_led3=self.io_led3,
                o_io_led4=self.io_led4,
                o_io_led5=self.io_led5,
                o_io_led6=self.io_led6,
                o_io_led7=self.io_led7)

platform = digilent_arty.Platform(variant="a7-35", toolchain="vivado")

top = TestHarness(platform)
platform.add_source("./generated/TestHarness.v")
platform.build(top)
