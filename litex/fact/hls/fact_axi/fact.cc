int fact(int x) {
#pragma HLS INTERFACE mode=s_axilite port=x      bundle=control
#pragma HLS INTERFACE mode=s_axilite port=return bundle=control
    int res = 1;
    hls_loop: for (int i = 2; i < x; i++) {
        res = res * i;
    }
    return res;
}
