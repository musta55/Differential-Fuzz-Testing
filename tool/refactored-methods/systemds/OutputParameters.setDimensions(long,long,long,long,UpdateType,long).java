public void setDimensions(long rows, long cols, long blen, long nnz, UpdateType update, long compressedSize) {
    setDimensions(rows, cols, blen, nnz, compressedSize, update, _linCacheCandidate);
}
// ---- helper method(s) introduced by the refactoring ----
private void setDimensions(long rows, long cols, long blen, long nnz, long compressedSize, UpdateType update, boolean linCacheCand) {
    _num_rows = rows;
    _num_cols = cols;
    _nnz = nnz;
    _blocksize = blen;
    _compressedSize = compressedSize;
    _updateType = update;
    _linCacheCandidate = linCacheCand;
    if (_blocksize == 0 || _blocksize == -1) {
        _blocked = false;
    } else if (_blocksize > 0) {
        _blocked = true;
    } else {
        throw new HopsException("In OutputParameters Lop, Invalid values for blocking dimensions: [" + _blocksize + "," + _blocksize + "].");
    }
}

