protected ReaderColumnSelectionDenseSingleBlockQuantized(MatrixBlock data, IColIndex colIndices, int rl, int ru, double[] scaleFactors) {
    super(colIndices, rl, Math.min(ru, data.getNumRows()) - 1);
    _data = data.getDenseBlockValues();
    _numCols = data.getNumColumns();
    _scaleFactors = scaleFactors;
}