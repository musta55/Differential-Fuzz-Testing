public void setDimensions(long rows, long cols, long blen, long nnz, UpdateType update) {
    _updateType = update;
    setDimensions(rows, cols, blen, nnz);
}