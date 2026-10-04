public void flushBufferToBinaryBlocks(ArrayList<Pair<Long, FrameBlock>> outList) throws DMLRuntimeException {
    if (_count == 0)
        return;
    sortBuffer();
    processAndOutputBlocks(outList);
    _count = 0;
}
// ---- helper method(s) introduced by the refactoring ----
private void sortBuffer() {
    Arrays.sort(_buff, 0, _count, new FrameReblockBufferComparator());
}

private void processAndOutputBlocks(ArrayList<Pair<Long, FrameBlock>> outList) throws DMLRuntimeException {
    Long tmpIx = -1L;
    FrameBlock tmpBlock = new FrameBlock(_schema);
    //current block indexes
    long cbi = -1, cbj = -1;
    for (int i = 0; i < _count; i++) {
        long bi = Math.max(UtilFunctions.computeBlockIndex(_buff[i].getRow(), _blen), 1);
        long bj = UtilFunctions.computeBlockIndex(_buff[i].getCol(), _blen);
        if (bi != cbi || bj != cbj) {
            outputBlockIfNecessary(outList, tmpIx, tmpBlock);
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
    outputBlockIfNecessary(outList, tmpIx, tmpBlock);
}

private void outputBlockIfNecessary(ArrayList<Pair<Long, FrameBlock>> outList, Long key, FrameBlock value) throws DMLRuntimeException {
    if (key != -1)
        outputBlock(outList, key, value);
}

