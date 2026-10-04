public List<IndexedMatrixValue> flushBufferToBinaryBlocks() throws DMLRuntimeException {
    if (_count == 0)
        return Collections.emptyList();
    sortBuffer();
    long numBlocks = countBlocks();
    return outputBlocks(numBlocks);
}
// ---- helper method(s) introduced by the refactoring ----
private void sortBuffer() {
    Arrays.sort(_buff, 0, _count, new ReblockBufferComparator());
}

private long countBlocks() {
    long numBlocks = 0;
    long cbi = -1, cbj = -1;
    for (int i = 0; i < _count; i++) {
        long bi = UtilFunctions.computeBlockIndex(_buff[i][0], _blen);
        long bj = UtilFunctions.computeBlockIndex(_buff[i][1], _blen);
        if (bi != cbi || bj != cbj) {
            cbi = bi;
            cbj = bj;
            numBlocks++;
        }
    }
    return numBlocks;
}

private List<IndexedMatrixValue> outputBlocks(long numBlocks) throws DMLRuntimeException {
    ArrayList<IndexedMatrixValue> ret = new ArrayList<>();
    boolean sparse = MatrixBlock.evalSparseFormatInMemory(_blen, _blen, _count / numBlocks);
    MatrixIndexes tmpIx = new MatrixIndexes();
    MatrixBlock tmpBlock = new MatrixBlock();
    long cbi = -1, cbj = -1;
    for (int i = 0; i < _count; i++) {
        long bi = UtilFunctions.computeBlockIndex(_buff[i][0], _blen);
        long bj = UtilFunctions.computeBlockIndex(_buff[i][1], _blen);
        if (bi != cbi || bj != cbj) {
            outputBlock(ret, tmpIx, tmpBlock);
            cbi = bi;
            cbj = bj;
            tmpIx = new MatrixIndexes(bi, bj);
            tmpBlock = new MatrixBlock(UtilFunctions.computeBlockSize(_rlen, bi, _blen), UtilFunctions.computeBlockSize(_clen, bj, _blen), sparse);
        }
        int ci = UtilFunctions.computeCellInBlock(_buff[i][0], _blen);
        int cj = UtilFunctions.computeCellInBlock(_buff[i][1], _blen);
        double tmp = Double.longBitsToDouble(_buff[i][2]);
        tmpBlock.appendValue(ci, cj, tmp);
    }
    outputBlock(ret, tmpIx, tmpBlock);
    _count = 0;
    return ret;
}

