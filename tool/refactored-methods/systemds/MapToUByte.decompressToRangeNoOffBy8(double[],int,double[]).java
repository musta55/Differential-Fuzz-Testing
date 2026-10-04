@Override
protected void decompressToRangeNoOffBy8(double[] c, int r, double[] values) {
    for (int i = 0; i < 8; i++) c[r + i] += values[_data[r + i]];
}
// ---- helper method(s) introduced by the refactoring ----
private void incrementCounts(int[] ret, int index) {
    for (int i = 0; i < 8; i++) ret[_data[index + i]]++;
}

