@Override
public Boolean call(Tuple2<MatrixIndexes, MatrixBlock> kv) throws Exception {
    for (int i = 0; i < _cols.length; i++) if (isInBlockRange(kv._1(), _cols[i]))
        return true;
    return false;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isInBlockRange(MatrixIndexes indexes, long col) {
    return UtilFunctions.isInBlockRange(indexes, _blen, 1, Long.MAX_VALUE, col, col);
}

