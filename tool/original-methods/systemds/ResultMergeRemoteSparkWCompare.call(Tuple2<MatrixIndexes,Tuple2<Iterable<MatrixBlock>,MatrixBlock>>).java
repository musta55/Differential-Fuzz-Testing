@Override
public Tuple2<MatrixIndexes, MatrixBlock> call(Tuple2<MatrixIndexes, Tuple2<Iterable<MatrixBlock>, MatrixBlock>> arg) throws Exception {
    MatrixIndexes ixin = arg._1();
    Iterator<MatrixBlock> din = arg._2()._1().iterator();
    MatrixBlock cin = arg._2()._2();
    //create compare array
    DenseBlock compare = DataConverter.convertToDenseBlock(cin, false);
    //merge all blocks into compare block
    MatrixBlock out = new MatrixBlock(cin);
    while (din.hasNext()) {
        if (_isAccum)
            mergeWithoutComp(out, din.next(), compare, false);
        else
            mergeWithComp(out, din.next(), compare);
    }
    //create output tuple
    return new Tuple2<>(new MatrixIndexes(ixin), out);
}