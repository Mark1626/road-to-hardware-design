
void ArrayPass(const int* in, int *out, int n) {
#pragma HLS INTERFACE mode=m_axi     port=in    bundle=gmem0 offset=slave
#pragma HLS INTERFACE mode=m_axi     port=out   bundle=gmem0 offset=slave
#pragma HLS INTERFACE mode=s_axilite port=in    bundle=control
#pragma HLS INTERFACE mode=s_axilite port=out   bundle=control
#pragma HLS INTERFACE mode=s_axilite port=n     bundle=control

	int temp[n];

    outer_loop: for (int i=0; i < n; i++) {
        temp[i] = 0;
        inner_loop: for (int j=0; j < n; j++) {
            temp[i] += in[i * n + j];
        }
        out[i] = temp[i];
    }
}
