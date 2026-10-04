public VarStats(long rlen, long clen, int blen, long nnz, boolean inmem) {
    initializeDataCharacteristics(rlen, clen, blen, nnz);
    setInMemoryStatus(inmem);
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeDataCharacteristics(long rlen, long clen, int blen, long nnz) {
    _dc = new MatrixCharacteristics(rlen, clen, blen, nnz);
}

private void setInMemoryStatus(boolean inmem) {
    _inmem = inmem;
}

