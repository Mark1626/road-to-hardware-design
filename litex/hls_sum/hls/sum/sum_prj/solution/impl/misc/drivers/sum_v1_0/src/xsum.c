// ==============================================================
// Vitis HLS - High-Level Synthesis from C, C++ and OpenCL v2022.2 (64-bit)
// Tool Version Limit: 2019.12
// Copyright 1986-2022 Xilinx, Inc. All Rights Reserved.
// ==============================================================
/***************************** Include Files *********************************/
#include "xsum.h"

/************************** Function Implementation *************************/
#ifndef __linux__
int XSum_CfgInitialize(XSum *InstancePtr, XSum_Config *ConfigPtr) {
    Xil_AssertNonvoid(InstancePtr != NULL);
    Xil_AssertNonvoid(ConfigPtr != NULL);

    InstancePtr->Control_BaseAddress = ConfigPtr->Control_BaseAddress;
    InstancePtr->IsReady = XIL_COMPONENT_IS_READY;

    return XST_SUCCESS;
}
#endif

void XSum_Start(XSum *InstancePtr) {
    u32 Data;

    Xil_AssertVoid(InstancePtr != NULL);
    Xil_AssertVoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    Data = XSum_ReadReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_AP_CTRL) & 0x80;
    XSum_WriteReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_AP_CTRL, Data | 0x01);
}

u32 XSum_IsDone(XSum *InstancePtr) {
    u32 Data;

    Xil_AssertNonvoid(InstancePtr != NULL);
    Xil_AssertNonvoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    Data = XSum_ReadReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_AP_CTRL);
    return (Data >> 1) & 0x1;
}

u32 XSum_IsIdle(XSum *InstancePtr) {
    u32 Data;

    Xil_AssertNonvoid(InstancePtr != NULL);
    Xil_AssertNonvoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    Data = XSum_ReadReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_AP_CTRL);
    return (Data >> 2) & 0x1;
}

u32 XSum_IsReady(XSum *InstancePtr) {
    u32 Data;

    Xil_AssertNonvoid(InstancePtr != NULL);
    Xil_AssertNonvoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    Data = XSum_ReadReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_AP_CTRL);
    // check ap_start to see if the pcore is ready for next input
    return !(Data & 0x1);
}

void XSum_EnableAutoRestart(XSum *InstancePtr) {
    Xil_AssertVoid(InstancePtr != NULL);
    Xil_AssertVoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    XSum_WriteReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_AP_CTRL, 0x80);
}

void XSum_DisableAutoRestart(XSum *InstancePtr) {
    Xil_AssertVoid(InstancePtr != NULL);
    Xil_AssertVoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    XSum_WriteReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_AP_CTRL, 0);
}

u32 XSum_Get_return(XSum *InstancePtr) {
    u32 Data;

    Xil_AssertNonvoid(InstancePtr != NULL);
    Xil_AssertNonvoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    Data = XSum_ReadReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_AP_RETURN);
    return Data;
}
void XSum_Set_arr(XSum *InstancePtr, u32 Data) {
    Xil_AssertVoid(InstancePtr != NULL);
    Xil_AssertVoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    XSum_WriteReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_ARR_DATA, Data);
}

u32 XSum_Get_arr(XSum *InstancePtr) {
    u32 Data;

    Xil_AssertNonvoid(InstancePtr != NULL);
    Xil_AssertNonvoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    Data = XSum_ReadReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_ARR_DATA);
    return Data;
}

void XSum_Set_n(XSum *InstancePtr, u32 Data) {
    Xil_AssertVoid(InstancePtr != NULL);
    Xil_AssertVoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    XSum_WriteReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_N_DATA, Data);
}

u32 XSum_Get_n(XSum *InstancePtr) {
    u32 Data;

    Xil_AssertNonvoid(InstancePtr != NULL);
    Xil_AssertNonvoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    Data = XSum_ReadReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_N_DATA);
    return Data;
}

void XSum_InterruptGlobalEnable(XSum *InstancePtr) {
    Xil_AssertVoid(InstancePtr != NULL);
    Xil_AssertVoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    XSum_WriteReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_GIE, 1);
}

void XSum_InterruptGlobalDisable(XSum *InstancePtr) {
    Xil_AssertVoid(InstancePtr != NULL);
    Xil_AssertVoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    XSum_WriteReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_GIE, 0);
}

void XSum_InterruptEnable(XSum *InstancePtr, u32 Mask) {
    u32 Register;

    Xil_AssertVoid(InstancePtr != NULL);
    Xil_AssertVoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    Register =  XSum_ReadReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_IER);
    XSum_WriteReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_IER, Register | Mask);
}

void XSum_InterruptDisable(XSum *InstancePtr, u32 Mask) {
    u32 Register;

    Xil_AssertVoid(InstancePtr != NULL);
    Xil_AssertVoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    Register =  XSum_ReadReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_IER);
    XSum_WriteReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_IER, Register & (~Mask));
}

void XSum_InterruptClear(XSum *InstancePtr, u32 Mask) {
    Xil_AssertVoid(InstancePtr != NULL);
    Xil_AssertVoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    XSum_WriteReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_ISR, Mask);
}

u32 XSum_InterruptGetEnabled(XSum *InstancePtr) {
    Xil_AssertNonvoid(InstancePtr != NULL);
    Xil_AssertNonvoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    return XSum_ReadReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_IER);
}

u32 XSum_InterruptGetStatus(XSum *InstancePtr) {
    Xil_AssertNonvoid(InstancePtr != NULL);
    Xil_AssertNonvoid(InstancePtr->IsReady == XIL_COMPONENT_IS_READY);

    return XSum_ReadReg(InstancePtr->Control_BaseAddress, XSUM_CONTROL_ADDR_ISR);
}

