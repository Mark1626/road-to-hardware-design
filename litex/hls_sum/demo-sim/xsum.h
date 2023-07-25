// Modifed file from
// ==============================================================
// Vitis HLS - High-Level Synthesis from C, C++ and OpenCL v2022.2 (64-bit)
// Tool Version Limit: 2019.12
// Copyright 1986-2022 Xilinx, Inc. All Rights Reserved.
// ==============================================================
#ifndef XSUM_H
#define XSUM_H

#ifdef __cplusplus
extern "C" {
#endif

/***************************** Include Files *********************************/
#include <stdint.h>
#include "xsum_hw.h"

/**************************** Type Definitions ******************************/

typedef uint8_t u8;
typedef uint16_t u16;
typedef uint32_t u32;
typedef uint64_t u64;

typedef struct {
    u16 DeviceId;
    u32 Control_BaseAddress;
} XSum_Config;

typedef struct {
    u32 Control_BaseAddress;
    u32 IsReady;
} XSum;

typedef u32 word_type;

/***************** Macros (Inline Functions) Definitions *********************/

#define XSum_WriteReg(BaseAddress, RegOffset, Data) \
    *(volatile u32*)((BaseAddress) + (RegOffset)) = (u32)(Data)
#define XSum_ReadReg(BaseAddress, RegOffset) \
    *(volatile u32*)((BaseAddress) + (RegOffset))

#define Xil_AssertVoid(expr)    (expr)
#define Xil_AssertNonvoid(expr) (expr)

#define XST_SUCCESS             0
#define XST_DEVICE_NOT_FOUND    2
#define XST_OPEN_DEVICE_FAILED  3
#define XIL_COMPONENT_IS_READY  1


/************************** Function Prototypes *****************************/
// #ifndef __linux__
int XSum_Initialize(XSum *InstancePtr, u16 DeviceId);
XSum_Config* XSum_LookupConfig(u16 DeviceId);
int XSum_CfgInitialize(XSum *InstancePtr, XSum_Config *ConfigPtr);
// #else
// int XSum_Initialize(XSum *InstancePtr, const char* InstanceName);
// int XSum_Release(XSum *InstancePtr);
// #endif

void XSum_Start(XSum *InstancePtr);
u32 XSum_IsDone(XSum *InstancePtr);
u32 XSum_IsIdle(XSum *InstancePtr);
u32 XSum_IsReady(XSum *InstancePtr);
void XSum_EnableAutoRestart(XSum *InstancePtr);
void XSum_DisableAutoRestart(XSum *InstancePtr);
u32 XSum_Get_return(XSum *InstancePtr);

void XSum_Set_arr(XSum *InstancePtr, u32 Data);
u32 XSum_Get_arr(XSum *InstancePtr);
void XSum_Set_n(XSum *InstancePtr, u32 Data);
u32 XSum_Get_n(XSum *InstancePtr);

void XSum_InterruptGlobalEnable(XSum *InstancePtr);
void XSum_InterruptGlobalDisable(XSum *InstancePtr);
void XSum_InterruptEnable(XSum *InstancePtr, u32 Mask);
void XSum_InterruptDisable(XSum *InstancePtr, u32 Mask);
void XSum_InterruptClear(XSum *InstancePtr, u32 Mask);
u32 XSum_InterruptGetEnabled(XSum *InstancePtr);
u32 XSum_InterruptGetStatus(XSum *InstancePtr);

#ifdef __cplusplus
}
#endif

#endif
