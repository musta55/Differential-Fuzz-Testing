public DnnParameters(int N, int C, int H, int W, int K, int R, int S, int stride_h, int stride_w, int pad_h, int pad_w, int numThreads) {
    this.N = N;
    this.C = C;
    this.H = H;
    this.W = W;
    this.K = K;
    this.R = R;
    this.S = S;
    this.stride_h = stride_h;
    this.stride_w = stride_w;
    this.pad_h = pad_h;
    this.pad_w = pad_w;
    if (H <= 0 || R <= 0 || stride_h < 0 || pad_h < 0)
        P = -1;
    else
        P = (int) DnnUtils.getP(H, R, stride_h, pad_h);
    if (W <= 0 || S <= 0 || stride_w < 0 || pad_w < 0)
        Q = -1;
    else
        Q = (int) DnnUtils.getQ(W, S, stride_w, pad_w);
    this.numThreads = numThreads;
}