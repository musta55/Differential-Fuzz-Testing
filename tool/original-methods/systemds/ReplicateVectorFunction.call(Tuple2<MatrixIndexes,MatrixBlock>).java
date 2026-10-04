@Override
public Iterator<Tuple2<MatrixIndexes, MatrixBlock>> call(Tuple2<MatrixIndexes, MatrixBlock> arg0) throws Exception {
    MatrixIndexes ix = arg0._1();
    MatrixBlock mb = arg0._2();
    //sanity check inputs
    if (_byRow && (ix.getRowIndex() != 1 || mb.getNumRows() > 1)) {
        throw new Exception("Expected a row vector in ReplicateVector");
    }
    if (!_byRow && (ix.getColumnIndex() != 1 || mb.getNumColumns() > 1)) {
        throw new Exception("Expected a column vector in ReplicateVector");
    }
    ArrayList<Tuple2<MatrixIndexes, MatrixBlock>> retVal = new ArrayList<>();
    for (int i = 1; i <= _numReplicas; i++) {
        if (_byRow)
            retVal.add(new Tuple2<>(new MatrixIndexes(i, ix.getColumnIndex()), mb));
        else
            retVal.add(new Tuple2<>(new MatrixIndexes(ix.getRowIndex(), i), mb));
    }
    return retVal.iterator();
}