@Override
public Iterator<Tuple2<MatrixIndexes, MatrixBlock>> call(Tuple2<MatrixIndexes, MatrixBlock> arg0) throws Exception {
    validateInput(arg0);
    return generateReplicatedVectors(arg0).iterator();
}
// ---- helper method(s) introduced by the refactoring ----
private void validateInput(Tuple2<MatrixIndexes, MatrixBlock> arg0) throws Exception {
    MatrixIndexes ix = arg0._1();
    MatrixBlock mb = arg0._2();
    if (_byRow && (ix.getRowIndex() != 1 || mb.getNumRows() > 1)) {
        throw new Exception("Expected a row vector in ReplicateVector");
    }
    if (!_byRow && (ix.getColumnIndex() != 1 || mb.getNumColumns() > 1)) {
        throw new Exception("Expected a column vector in ReplicateVector");
    }
}

private ArrayList<Tuple2<MatrixIndexes, MatrixBlock>> generateReplicatedVectors(Tuple2<MatrixIndexes, MatrixBlock> arg0) {
    MatrixIndexes ix = arg0._1();
    MatrixBlock mb = arg0._2();
    ArrayList<Tuple2<MatrixIndexes, MatrixBlock>> retVal = new ArrayList<>();
    for (int i = 1; i <= _numReplicas; i++) {
        if (_byRow) {
            retVal.add(new Tuple2<>(new MatrixIndexes(i, ix.getColumnIndex()), mb));
        } else {
            retVal.add(new Tuple2<>(new MatrixIndexes(ix.getRowIndex(), i), mb));
        }
    }
    return retVal;
}

