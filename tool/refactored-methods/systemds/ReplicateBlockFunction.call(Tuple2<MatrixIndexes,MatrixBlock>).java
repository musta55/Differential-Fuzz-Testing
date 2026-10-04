@Override
public Iterator<Tuple2<MatrixIndexes, MatrixBlock>> call(Tuple2<MatrixIndexes, MatrixBlock> arg0) throws Exception {
    ArrayList<Tuple2<MatrixIndexes, MatrixBlock>> ret = new ArrayList<>();
    MatrixIndexes ixIn = arg0._1();
    MatrixBlock blkIn = arg0._2();
    long numBlocks = (long) Math.ceil((double) _len / _blen);
    if (_left) {
        handleLeftMatrix(ixIn, blkIn, numBlocks, ret);
    } else {
        handleRightMatrix(ixIn, blkIn, numBlocks, ret);
    }
    //output list of new tuples
    return ret.iterator();
}
// ---- helper method(s) introduced by the refactoring ----
private void handleLeftMatrix(MatrixIndexes ixIn, MatrixBlock blkIn, long numBlocks, ArrayList<Tuple2<MatrixIndexes, MatrixBlock>> ret) {
    long i = ixIn.getRowIndex();
    for (long j = 1; j <= numBlocks; j++) {
        MatrixIndexes tmpix = new MatrixIndexes(i, j);
        MatrixBlock tmpblk = _deep ? new MatrixBlock(blkIn) : blkIn;
        ret.add(new Tuple2<>(tmpix, tmpblk));
    }
}

private void handleRightMatrix(MatrixIndexes ixIn, MatrixBlock blkIn, long numBlocks, ArrayList<Tuple2<MatrixIndexes, MatrixBlock>> ret) {
    long j = ixIn.getColumnIndex();
    for (long i = 1; i <= numBlocks; i++) {
        MatrixIndexes tmpix = new MatrixIndexes(i, j);
        MatrixBlock tmpblk = _deep ? new MatrixBlock(blkIn) : blkIn;
        ret.add(new Tuple2<>(tmpix, tmpblk));
    }
}

