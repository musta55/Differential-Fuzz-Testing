@Override
public Boolean call(Tuple2<MatrixIndexes, MatrixBlock> arg0) throws Exception {
    //returns true for matrix blocks on matrix diagonal
    MatrixIndexes ix = arg0._1();
    return (ix.getRowIndex() == ix.getColumnIndex());
}