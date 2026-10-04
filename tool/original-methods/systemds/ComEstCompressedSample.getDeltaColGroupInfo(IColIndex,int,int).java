@Override
public CompressedSizeInfoColGroup getDeltaColGroupInfo(IColIndex colIndexes, int estimate, int nrUniqueUpperBound) {
    if (!loggedWarning)
        LOG.warn("Compressed input cannot fallback to resampling " + colIndexes);
    return null;
}