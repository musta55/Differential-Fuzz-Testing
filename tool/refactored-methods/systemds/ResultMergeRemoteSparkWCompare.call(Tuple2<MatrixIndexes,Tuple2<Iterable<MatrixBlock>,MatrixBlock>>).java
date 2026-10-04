@Override
public Tuple2<MatrixIndexes, MatrixBlock> call(Tuple2<MatrixIndexes, Tuple2<Iterable<MatrixBlock>, MatrixBlock>> arg) throws Exception {
    MatrixIndexes ixin = arg._1();
    Iterator<MatrixBlock> din = arg._2()._1().iterator();
    MatrixBlock cin = arg._2()._2();
    DenseBlock compare = createCompareArray(cin);
    MatrixBlock out = mergeBlocks(din, cin, compare);
    return new Tuple2<>(new MatrixIndexes(ixin), out);
}
// ---- helper method(s) introduced by the refactoring ----
private DenseBlock createCompareArray(MatrixBlock cin) {
    return DataConverter.convertToDenseBlock(cin, false);
}

private MatrixBlock mergeBlocks(Iterator<MatrixBlock> din, MatrixBlock cin, DenseBlock compare) {
    MatrixBlock out = new MatrixBlock(cin);
    while (din.hasNext()) {
        if (_isAccum)
            mergeWithoutComp(out, din.next(), compare, false);
        else
            mergeWithComp(out, din.next(), compare);
    }
    return out;
}

