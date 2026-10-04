@Override
public void decompressVec(int nCol, double[] c, int off, double[] values, int rowIdx) {
    for (int j = 0; j < nCol; j++) c[off + get(j)] += values[rowIdx + j];
}