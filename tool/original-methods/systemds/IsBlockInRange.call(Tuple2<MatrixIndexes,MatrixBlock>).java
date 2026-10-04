@Override
public Boolean call(Tuple2<MatrixIndexes, MatrixBlock> kv) throws Exception {
    return UtilFunctions.isInBlockRange(kv._1(), _blen, _rl, _ru, _cl, _cu);
}