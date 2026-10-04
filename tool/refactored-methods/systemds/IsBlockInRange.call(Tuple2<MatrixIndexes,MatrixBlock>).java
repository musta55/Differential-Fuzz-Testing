@Override
public Boolean call(Tuple2<MatrixIndexes, MatrixBlock> keyValue) throws Exception {
    return UtilFunctions.isInBlockRange(keyValue._1(), blockSize, rowLowerBound, rowUpperBound, columnLowerBound, columnUpperBound);
}