public void flushBufferToBinaryBlocks(ArrayList<Pair<Long, FrameBlock>> outList) throws DMLRuntimeException {
    if (_count == 0)
        return;
    //Step 1) sort reblock buffer (blockwise, no in-block sorting!)
    Arrays.sort(_buff, 0, _count, new FrameReblockBufferComparator());
    //Step 2) output blocks
    Long tmpIx = -1L;
    FrameBlock tmpBlock = new FrameBlock(_schema);
    //put values into block and output
    //current block indexes
    long cbi = -1, cbj = -1;
    for (int i = 0; i < _count; i++) {
        //compute block indexes (w/ robustness for meta data handling)
        long bi = Math.max(UtilFunctions.computeBlockIndex(_buff[i].getRow(), _blen), 1);
        long bj = UtilFunctions.computeBlockIndex(_buff[i].getCol(), _blen);
        //output block and switch to next index pair
        if (bi != cbi || bj != cbj) {
            if (cbi != -1 && cbj != -1)
                outputBlock(outList, tmpIx, tmpBlock);
            cbi = bi;
            cbj = bj;
            tmpIx = (bi - 1) * _blen + 1;
            tmpBlock = new FrameBlock(_schema);
            tmpBlock.ensureAllocatedColumns(Math.min(_blen, (int) (_rlen - (bi - 1) * _blen)));
        }
        int ci = UtilFunctions.computeCellInBlock(_buff[i].getRow(), _blen);
        int cj = UtilFunctions.computeCellInBlock(_buff[i].getCol(), _blen);
        String bv = (String) _buff[i].getObjVal();
        if (ci == -3)
            tmpBlock.getColumnMetadata(cj).setMvValue(bv);
        else if (ci == -2)
            tmpBlock.getColumnMetadata(cj).setNumDistinct(Long.parseLong(bv));
        else
            tmpBlock.set(ci, cj, bv);
    }
    //output last block
    if (cbi != -1 && cbj != -1)
        outputBlock(outList, tmpIx, tmpBlock);
    _count = 0;
}