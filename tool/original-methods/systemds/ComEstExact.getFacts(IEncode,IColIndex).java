protected CompressedSizeInfoColGroup getFacts(IEncode map, IColIndex colIndexes) {
    final int _numRows = getNumRows();
    final EstimationFactors em = map.extractFacts(_numRows, _data.getSparsity(), _data.getSparsity(), _cs);
    return new CompressedSizeInfoColGroup(colIndexes, em, _cs.validCompressions, map);
}