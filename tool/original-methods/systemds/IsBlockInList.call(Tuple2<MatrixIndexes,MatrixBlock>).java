@Override
public Boolean call(Tuple2<MatrixIndexes, MatrixBlock> kv) throws Exception {
    for (int i = 0; i < _cols.length; i++) if (UtilFunctions.isInBlockRange(kv._1(), _blen, 1, Long.MAX_VALUE, _cols[i], _cols[i]))
        return true;
    return false;
}