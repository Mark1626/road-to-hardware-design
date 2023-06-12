from migen import *

from litex.soc.interconnect.csr import *
from litex.soc.cores import gpio

class Led(gpio.GPIOOut):
    pass

class Button(gpio.GPIOOut):
    pass

class Switch(gpio.GPIOOut):
    pass
