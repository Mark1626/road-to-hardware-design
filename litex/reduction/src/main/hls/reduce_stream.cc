#include "hls_stream.h"
#include "ap_int.h"

int reduce_stream(hls::stream<ap_uint<32>>& arr, int n) {
  int i;
  int res = 0;

  #pragma nounroll
  for(i = 0; i < n; ++i) {
    res += arr.read();
  }

  return res;
}
