@Override
public Boolean call(Tuple2<MatrixIndexes, MatrixBlock> arg0) throws Exception {
    //always keep 1-1 block in order to prevent empty rdds
    boolean isOneOneBlock = (arg0._1().getRowIndex() == 1 && arg0._1().getColumnIndex() == 1);
    //returns true for non-empty matrix blocks
    return !arg0._2().isEmptyBlock(false) || isOneOneBlock;
}