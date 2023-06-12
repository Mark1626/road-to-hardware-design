#!/usr/bin/env python3

import time
import random

from litex import RemoteClient

wb = RemoteClient()
wb.open()

# # #

# Test led
print("Testing Led...")
for i in range(8):
    wb.regs.leds_out.write(i)
    time.sleep(1)

# # #

wb.close()
