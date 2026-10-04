public List<IndexedMatrixValue> flushBufferToBinaryBlocks() throws DMLRuntimeException {
    if (_count == 0)
        return Collections.emptyList();
    //Step 1) sort reblock buffer (blockwise, no in-block sorting!)
    Arrays.sort(_buff, 0, _count, new ReblockBufferComparator());
    //Step 2) scan for number of created blocks
    //number of blocks in buffer
    long numBlocks = 0;
    //current block indexes
    long cbi = -1, cbj = -1;
    for (int i = 0; i < _count; i++) {
        long bi = UtilFunctions.computeBlockIndex(_buff[i][0], _blen);
        long bj = UtilFunctions.computeBlockIndex(_buff[i][1], _blen);
        //switch to next block
        if (bi != cbi || bj != cbj) {
            cbi = bi;
            cbj = bj;
            numBlocks++;
        }
    }
    //Step 3) output blocks
    ArrayList<IndexedMatrixValue> ret = new ArrayList<>();
    boolean sparse = MatrixBlock.evalSparseFormatInMemory(_blen, _blen, _count / numBlocks);
    MatrixIndexes tmpIx = new MatrixIndexes();
    MatrixBlock tmpBlock = new MatrixBlock();
    //put values into block and output
    //current block indexes
    cbi = -1;
    //current block indexes
    cbj = -1;
    for (int i = 0; i < _count; i++) {
        long bi = UtilFunctions.computeBlockIndex(_buff[i][0], _blen);
        long bj = UtilFunctions.computeBlockIndex(_buff[i][1], _blen);
        //output block and switch to next index pair
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
    //output last block
    outputBlock(ret, tmpIx, tmpBlock);
    _count = 0;
    return ret;
}