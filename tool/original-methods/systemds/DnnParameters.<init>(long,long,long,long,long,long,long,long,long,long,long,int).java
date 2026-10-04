public DnnParameters(long N, long C, long H, long W, long K, long R, long S, long stride_h, long stride_w, long pad_h, long pad_w, int numThreads) {
    this.N = convertToInt(N);
    this.C = convertToInt(C);
    this.H = convertToInt(H);
    this.W = convertToInt(W);
    this.K = convertToInt(K);
    this.R = convertToInt(R);
    this.S = convertToInt(S);
    this.stride_h = convertToInt(stride_h);
    this.stride_w = convertToInt(stride_w);
    this.pad_h = convertToInt(pad_h);
    this.pad_w = convertToInt(pad_w);
    if (H >= 0 && pad_h >= 0 && R >= 0 && stride_h >= 0)
        P = (int) ((H + 2 * pad_h - R) / stride_h + 1);
    else
        P = -1;
    if (W >= 0 && pad_w >= 0 && S >= 0 && stride_w >= 0)
        Q = (int) ((W + 2 * pad_w - S) / stride_w + 1);
    else
        Q = -1;
    this.numThreads = numThreads;
}