@Override
public void decompressVec(int nCol, double[] c, int off, double[] values, int rowIdx) {
    for (int j = 0; j < nCol; j++) {
        c[off + get(j)] += values[rowIdx + j];
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean allIndicesPresent(IColIndex index) {
    IIterate iterator = index.iterator();
    while (iterator.hasNext()) {
        if (findIndex(iterator.next()) < 0) {
            return false;
        }
    }
    return true;
}

