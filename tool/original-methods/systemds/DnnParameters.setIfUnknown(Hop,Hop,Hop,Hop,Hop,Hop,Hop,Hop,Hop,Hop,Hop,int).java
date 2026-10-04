public void setIfUnknown(Hop N, Hop C, Hop H, Hop W, Hop K, Hop R, Hop S, Hop stride_h, Hop stride_w, Hop pad_h, Hop pad_w, int numThreads) {
    if (this.N < 0)
        this.N = convertToInt(Hop.computeSizeInformation(N));
    if (this.C < 0)
        this.C = convertToInt(Hop.computeSizeInformation(C));
    if (this.H < 0)
        this.H = convertToInt(Hop.computeSizeInformation(H));
    if (this.W < 0)
        this.W = convertToInt(Hop.computeSizeInformation(W));
    if (this.K < 0)
        this.K = convertToInt(Hop.computeSizeInformation(K));
    if (this.R < 0)
        this.R = convertToInt(Hop.computeSizeInformation(R));
    if (this.S < 0)
        this.S = convertToInt(Hop.computeSizeInformation(S));
    if (this.stride_h < 0)
        this.stride_h = convertToInt(Hop.computeSizeInformation(stride_h));
    if (this.stride_w < 0)
        this.stride_w = convertToInt(Hop.computeSizeInformation(stride_w));
    if (this.pad_h < 0)
        this.pad_h = convertToInt(Hop.computeSizeInformation(pad_h));
    if (this.pad_w < 0)
        this.pad_w = convertToInt(Hop.computeSizeInformation(pad_w));
    if (this.P < 0 && this.H >= 0 && this.R >= 0 && this.stride_h >= 0 && this.pad_h >= 0) {
        this.P = (int) DnnUtils.getP(this.H, this.R, this.stride_h, this.pad_h);
    }
    if (this.Q < 0 && this.W >= 0 && this.S >= 0 && this.stride_w >= 0 && this.pad_w >= 0) {
        this.Q = (int) DnnUtils.getQ(this.W, this.S, this.stride_w, this.pad_w);
    }
    this.numThreads = numThreads;
}