@Override
protected void decompressToRangeNoOffBy8(double[] c, int r, double[] values) {
    c[r] += values[_data[r]];
    c[r + 1] += values[_data[r + 1]];
    c[r + 2] += values[_data[r + 2]];
    c[r + 3] += values[_data[r + 3]];
    c[r + 4] += values[_data[r + 4]];
    c[r + 5] += values[_data[r + 5]];
    c[r + 6] += values[_data[r + 6]];
    c[r + 7] += values[_data[r + 7]];
}