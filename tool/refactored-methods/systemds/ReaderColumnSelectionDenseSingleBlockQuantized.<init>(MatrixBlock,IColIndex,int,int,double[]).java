protected ReaderColumnSelectionDenseSingleBlockQuantized(MatrixBlock data, IColIndex colIndices, int rl, int ru, double[] scaleFactors) {
    super(colIndices, rl, Math.min(ru, data.getNumRows()) - 1);
    _data = data.getDenseBlockValues();
    _numCols = data.getNumColumns();
    _scaleFactors = scaleFactors;
    reusableArr = new double[colIndices.size()];
    reusableReturn = new DblArray(reusableArr);
}
// ---- helper method(s) introduced by the refactoring ----
private double getScaleFactor(int row) {
    return _scaleFactors.length == 1 ? _scaleFactors[0] : _scaleFactors[row];
}

