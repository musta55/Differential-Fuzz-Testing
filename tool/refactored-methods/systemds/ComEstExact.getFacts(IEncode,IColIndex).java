protected CompressedSizeInfoColGroup getFacts(IEncode map, IColIndex colIndexes) {
    int numRows = getNumRows();
    EstimationFactors em = map.extractFacts(numRows, _data.getSparsity(), _data.getSparsity(), _cs);
    return new CompressedSizeInfoColGroup(colIndexes, em, _cs.validCompressions, map);
}