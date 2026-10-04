public void setDimensions(long rows, long cols, long blen, long nnz, boolean linCacheCand) {
    _linCacheCandidate = linCacheCand;
    setDimensions(rows, cols, blen, nnz);
}