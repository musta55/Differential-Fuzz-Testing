@Override
public CompressedSizeInfoColGroup getColGroupInfo(IColIndex colIndexes, int estimate, int nrUniqueUpperBound) {
    if (!loggedWarning)
        LOG.warn("Compressed input cannot fallback to resampling " + colIndexes);
    loggedWarning = true;
    return null;
}