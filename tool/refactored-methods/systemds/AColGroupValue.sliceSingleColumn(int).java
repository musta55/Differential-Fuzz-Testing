@Override
protected AColGroup sliceSingleColumn(int idx) {
    IColIndex retIndexes = createSingleColumnIndex();
    if (_colIndexes.size() == 1)
        return copyAndSet(retIndexes, _dict);
    IDictionary retDict = sliceDictionaryForSingleColumn(idx);
    return createColGroupForSingleColumn(retIndexes, retDict);
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

