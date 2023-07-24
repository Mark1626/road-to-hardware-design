int fact(int x) {
    int res = 1;
    hls_loop: for (int i = 2; i < x; i++) {
        res = res * i;
    }
    return res;
}
