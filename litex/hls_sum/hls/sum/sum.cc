int sum(int *arr, int n) {
#pragma HLS INTERFACE mode=m_axi      port=arr      bundle=gmem0   offset=slave
#pragma HLS interface mode=s_axilite  port=arr      bundle=control
#pragma HLS INTERFACE mode=s_axilite  port=n        bundle=control
#pragma HLS INTERFACE mode=s_axilite  port=return   bundle=control

    int res = 0;
    sum_loop: for (int i = 0; i < n; i++) {
        res += arr[i];
    }
    return res;
}
