#include "hls_task.h"
#include "hls_stream.h"

#include "repeater.h"

void repeater(hls::stream<int> &in_data, hls::stream<int> &out_data) {
    int val = in_data.read();
    out_data.write(val);
    out_data.write(val);
}

void LoadData(const int *in, hls::stream<int> &in_data, int n) {
    for (int idx = 0; idx < n; idx++) {
        int temp = in[idx];
        in_data.write(temp);
    }
}

void StoreData(hls::stream<int> &out_data, int *out, int n) {
    for (int idx = 0; idx < 2*n; idx++) {
        int temp = out_data.read();
        out[idx] = temp;
    }
}

void RepeaterTop(const int* in, int *out, int n) {

#pragma HLS INTERFACE mode=m_axi     port=in    bundle=gmem0 offset=slave
#pragma HLS INTERFACE mode=m_axi     port=out   bundle=gmem0 offset=slave
#pragma HLS INTERFACE mode=s_axilite port=in    bundle=control
#pragma HLS INTERFACE mode=s_axilite port=out   bundle=control
#pragma HLS INTERFACE mode=s_axilite port=n     bundle=control

    hls::stream<int> in_data;
    hls::stream<int> out_data;

#pragma HLS DATAFLOW
    LoadData(in, in_data, n);
    hls_thread_local hls::task t2(repeater, in_data, out_data);
    StoreData(out_data, out, n);

}
