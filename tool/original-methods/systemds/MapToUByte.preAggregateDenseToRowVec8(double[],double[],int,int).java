@Override
protected void preAggregateDenseToRowVec8(double[] mV, double[] preAV, int rc, int off) {
    preAV[getIndex(rc)] += mV[off];
    preAV[getIndex(rc + 1)] += mV[off + 1];
    preAV[getIndex(rc + 2)] += mV[off + 2];
    preAV[getIndex(rc + 3)] += mV[off + 3];
    preAV[getIndex(rc + 4)] += mV[off + 4];
    preAV[getIndex(rc + 5)] += mV[off + 5];
    preAV[getIndex(rc + 6)] += mV[off + 6];
    preAV[getIndex(rc + 7)] += mV[off + 7];
}