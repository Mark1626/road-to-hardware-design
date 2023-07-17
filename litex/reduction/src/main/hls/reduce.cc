#include "hls_stream.h"
#include "ap_int.h"

#pragma HLS_interface arr fifo
int reduce(ap_uint<32> *arr, int n) {
  int i;
  int res = 0;

  #pragma nounroll
  for(i = 0; i < n; ++i) {
    res += arr[i];
  }

  return res;
}
