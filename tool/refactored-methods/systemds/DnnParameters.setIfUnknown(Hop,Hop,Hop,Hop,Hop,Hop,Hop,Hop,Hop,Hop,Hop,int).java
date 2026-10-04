public void setIfUnknown(Hop N, Hop C, Hop H, Hop W, Hop K, Hop R, Hop S, Hop stride_h, Hop stride_w, Hop pad_h, Hop pad_w, int numThreads) {
    setFieldIfUnknown(this::getN, this::setN, N);
    setFieldIfUnknown(this::getC, this::setC, C);
    setFieldIfUnknown(this::getH, this::setH, H);
    setFieldIfUnknown(this::getW, this::setW, W);
    setFieldIfUnknown(this::getK, this::setK, K);
    setFieldIfUnknown(this::getR, this::setR, R);
    setFieldIfUnknown(this::getS, this::setS, S);
    setFieldIfUnknown(this::getStride_h, this::setStride_h, stride_h);
    setFieldIfUnknown(this::getStride_w, this::setStride_w, stride_w);
    setFieldIfUnknown(this::getPad_h, this::setPad_h, pad_h);
    setFieldIfUnknown(this::getPad_w, this::setPad_w, pad_w);
    if (this.P < 0 && this.H >= 0 && this.R >= 0 && this.stride_h >= 0 && this.pad_h >= 0) {
        this.P = (int) DnnUtils.getP(this.H, this.R, this.stride_h, this.pad_h);
    }
    if (this.Q < 0 && this.W >= 0 && this.S >= 0 && this.stride_w >= 0 && this.pad_w >= 0) {
        this.Q = (int) DnnUtils.getQ(this.W, this.S, this.stride_w, this.pad_w);
    }
    this.numThreads = numThreads;
}
// ---- helper method(s) introduced by the refactoring ----
private int calculateP(long H, long pad_h, long R, long stride_h) {
    if (H >= 0 && pad_h >= 0 && R >= 0 && stride_h >= 0)
        return (int) ((H + 2 * pad_h - R) / stride_h + 1);
    else
        return -1;
}

private int calculateQ(long W, long pad_w, long S, long stride_w) {
    if (W >= 0 && pad_w >= 0 && S >= 0 && stride_w >= 0)
        return (int) ((W + 2 * pad_w - S) / stride_w + 1);
    else
        return -1;
}

private void setFieldIfUnknown(java.util.function.IntSupplier getter, java.util.function.IntConsumer setter, Hop hop) {
    if (getter.getAsInt() < 0) {
        setter.accept(convertToInt(Hop.computeSizeInformation(hop)));
    }
}

private int getN() {
    return N;
}

private void setN(int n) {
    this.N = n;
}

private int getC() {
    return C;
}

private void setC(int c) {
    this.C = c;
}

private int getH() {
    return H;
}

private void setH(int h) {
    this.H = h;
}

private int getW() {
    return W;
}

private void setW(int w) {
    this.W = w;
}

private int getK() {
    return K;
}

private void setK(int k) {
    this.K = k;
}

private int getR() {
    return R;
}

private void setR(int r) {
    this.R = r;
}

private int getS() {
    return S;
}

private void setS(int s) {
    this.S = s;
}

private int getStride_h() {
    return stride_h;
}

private void setStride_h(int stride_h) {
    this.stride_h = stride_h;
}

private int getStride_w() {
    return stride_w;
}

private void setStride_w(int stride_w) {
    this.stride_w = stride_w;
}

private int getPad_h() {
    return pad_h;
}

private void setPad_h(int pad_h) {
    this.pad_h = pad_h;
}

private int getPad_w() {
    return pad_w;
}

private void setPad_w(int pad_w) {
    this.pad_w = pad_w;
}

