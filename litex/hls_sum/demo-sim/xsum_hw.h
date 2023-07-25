// ==============================================================
// Vitis HLS - High-Level Synthesis from C, C++ and OpenCL v2022.2 (64-bit)
// Tool Version Limit: 2019.12
// Copyright 1986-2022 Xilinx, Inc. All Rights Reserved.
// ==============================================================
// control
// 0x00 : Control signals
//        bit 0  - ap_start (Read/Write/COH)
//        bit 1  - ap_done (Read/COR)
//        bit 2  - ap_idle (Read)
//        bit 3  - ap_ready (Read/COR)
//        bit 7  - auto_restart (Read/Write)
//        bit 9  - interrupt (Read)
//        others - reserved
// 0x04 : Global Interrupt Enable Register
//        bit 0  - Global Interrupt Enable (Read/Write)
//        others - reserved
// 0x08 : IP Interrupt Enable Register (Read/Write)
//        bit 0 - enable ap_done interrupt (Read/Write)
//        bit 1 - enable ap_ready interrupt (Read/Write)
//        others - reserved
// 0x0c : IP Interrupt Status Register (Read/TOW)
//        bit 0 - ap_done (Read/TOW)
//        bit 1 - ap_ready (Read/TOW)
//        others - reserved
// 0x10 : Data signal of ap_return
//        bit 31~0 - ap_return[31:0] (Read)
// 0x18 : Data signal of arr
//        bit 31~0 - arr[31:0] (Read/Write)
// 0x1c : reserved
// 0x20 : Data signal of n
//        bit 31~0 - n[31:0] (Read/Write)
// 0x24 : reserved
// (SC = Self Clear, COR = Clear on Read, TOW = Toggle on Write, COH = Clear on Handshake)

#define XSUM_CONTROL_ADDR_AP_CTRL   0x00
#define XSUM_CONTROL_ADDR_GIE       0x04
#define XSUM_CONTROL_ADDR_IER       0x08
#define XSUM_CONTROL_ADDR_ISR       0x0c
#define XSUM_CONTROL_ADDR_AP_RETURN 0x10
#define XSUM_CONTROL_BITS_AP_RETURN 32
#define XSUM_CONTROL_ADDR_ARR_DATA  0x18
#define XSUM_CONTROL_BITS_ARR_DATA  32
#define XSUM_CONTROL_ADDR_N_DATA    0x20
#define XSUM_CONTROL_BITS_N_DATA    32

