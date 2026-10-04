@Override
protected void tsmm(double[] result, int numColumns, int nRows) {
    int[] counts = getCounts();
    tsmm(result, numColumns, counts, _dict, _colIndexes);
}
// ---- helper method(s) introduced by the refactoring ----
private IColIndex createSingleColumnIndex() {
    return ColIndexFactory.create(1);
}

private IDictionary sliceDictionaryForSingleColumn(int idx) {
    return _dict.sliceOutColumnRange(idx, idx + 1, _colIndexes.size());
}

private AColGroup createColGroupForSingleColumn(IColIndex retIndexes, IDictionary retDict) {
    if (retDict == null)
        return new ColGroupEmpty(retIndexes);
    else
        return copyAndSet(retIndexes, retDict);
}

private void validateSliceMultiColumns(IDictionary retDict, IColIndex outputCols) {
    if (retDict.getNumberOfValues(outputCols.size()) != getNumValues())
        throw new DMLCompressionException("Invalid Slice Multi Columns");
}

private IColIndex createExpandedColumnIndex(IDictionary d) {
    return ColIndexFactory.create(d.getNumberOfColumns(_dict.getNumberOfValues(1)));
}

