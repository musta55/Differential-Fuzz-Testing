public VarStats(long rlen, long clen, int blen, long nnz, boolean inmem) {
    _dc = new MatrixCharacteristics(rlen, clen, blen, nnz);
    _inmem = inmem;
}