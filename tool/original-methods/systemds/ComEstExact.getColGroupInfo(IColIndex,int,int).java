@Override
public CompressedSizeInfoColGroup getColGroupInfo(IColIndex colIndexes, int estimate, int nrUniqueUpperBound) {
    final IEncode map = EncodingFactory.createFromMatrixBlock(_data, _cs.transposed, colIndexes, _cs.scaleFactors);
    if (map instanceof EmptyEncoding)
        return new CompressedSizeInfoColGroup(colIndexes, getNumRows(), CompressionType.EMPTY);
    return getFacts(map, colIndexes);
}