#!/usr/bin/env python3

import argparse

from migen import *

from litex.build.generic_platform import *

class TestHarness(Module):
    def __init__(self, pads):
        self.clock = ClockSignal()
        self.reset = pads["reset"]

        self.serial = pads["serial"]

        self.io_txd = self.serial.tx
        self.io_rxd = self.serial.rx
        self.io_led0 = pads["io_led0"]
        self.io_led1 = pads["io_led1"]
        self.io_led2 = pads["io_led2"]
        self.io_led3 = pads["io_led3"]
        self.io_led4 = pads["io_led4"]
        self.io_led5 = pads["io_led5"]
        self.io_led6 = pads["io_led6"]
        self.io_led7 = pads["io_led7"]

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

# Build --------------------------------------------------------------------------------------------

def main():
    parser = argparse.ArgumentParser(description='Blackbox Uart Echo example')
    parser.add_argument('--board', type=str, default='arty', required=True, help='Name of the board (default: arty)')
    parser.add_argument('--help-boards', action='store_true', help='Display list of supported boards')

    args = parser.parse_args()

    if args.help_boards:
        print('Supported boards:')
        print('arty - Digilent Arty A7 35T')
        print('alchitry-cu - Sparkfun Alchitry Cu')
        exit(0)

    board_name = args.board

    platform = None
    pads = {}

    if board_name == "arty":
        from litex_boards.platforms import digilent_arty
        platform = digilent_arty.Platform(variant="a7-35", toolchain="vivado")

        pads["reset"]       = platform.request("cpu_reset")
        pads["serial"]      = platform.request("serial")
        pads["io_led0"]     = platform.request("user_led", 0)
        pads["io_led1"]     = platform.request("user_led", 1)
        pads["io_led2"]     = platform.request("user_led", 2)
        pads["io_led3"]     = platform.request("user_led", 3)
        pads["io_led4"]     = platform.request("rgb_led", 0).r
        pads["io_led5"]     = platform.request("rgb_led", 1).r
        pads["io_led6"]     = platform.request("rgb_led", 2).r
        pads["io_led7"]     = platform.request("rgb_led", 3).r

    elif board_name == "alchitry-cu":
        from litex_boards.platforms import alchitry_cu
        platform = alchitry_cu.Platform()

        pads["reset"]       = platform.request("cpu_reset")
        pads["serial"]      = platform.request("serial")
        pads["io_led0"]     = platform.request("user_led", 0)
        pads["io_led1"]     = platform.request("user_led", 1)
        pads["io_led2"]     = platform.request("user_led", 2)
        pads["io_led3"]     = platform.request("user_led", 3)
        pads["io_led4"]     = platform.request("user_led", 4)
        pads["io_led5"]     = platform.request("user_led", 5)
        pads["io_led6"]     = platform.request("user_led", 6)
        pads["io_led7"]     = platform.request("user_led", 7)

    else:
        print("Unknown platform")
        exit(1)

    top = TestHarness(pads)
    platform.add_source("./generated/TestHarness.v")
    platform.build(top)


if __name__ == "__main__":
    main()
