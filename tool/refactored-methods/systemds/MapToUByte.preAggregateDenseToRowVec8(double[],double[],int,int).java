@Override
protected void preAggregateDenseToRowVec8(double[] mV, double[] preAV, int rc, int off) {
    for (int i = 0; i < 8; i++) preAV[getIndex(rc + i)] += mV[off + i];
}
// ---- helper method(s) introduced by the refactoring ----
private void incrementCounts(int[] ret, int index) {
    for (int i = 0; i < 8; i++) ret[_data[index + i]]++;
}

