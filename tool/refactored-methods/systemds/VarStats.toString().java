@Override
public String toString() {
    return new StringBuilder("VarStats: [").append("rlen = ").append(_dc.getRows()).append(", clen = ").append(_dc.getCols()).append(", nnz = ").append(_dc.getNonZeros()).append(", inmem = ").append(_inmem).append("]").toString();
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeDataCharacteristics(long rlen, long clen, int blen, long nnz) {
    _dc = new MatrixCharacteristics(rlen, clen, blen, nnz);
}

private void setInMemoryStatus(boolean inmem) {
    _inmem = inmem;
}

