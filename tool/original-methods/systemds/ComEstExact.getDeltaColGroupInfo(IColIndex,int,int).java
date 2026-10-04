@Override
public CompressedSizeInfoColGroup getDeltaColGroupInfo(IColIndex colIndexes, int estimate, int nrUniqueUpperBound) {
    final IEncode map = EncodingFactory.createFromMatrixBlockDelta(_data, _cs.transposed, colIndexes);
    return getFacts(map, colIndexes);
}