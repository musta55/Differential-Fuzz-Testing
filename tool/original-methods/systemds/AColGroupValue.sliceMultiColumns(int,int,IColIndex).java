@Override
protected AColGroup sliceMultiColumns(int idStart, int idEnd, IColIndex outputCols) {
    final IDictionary retDict = _dict.sliceOutColumnRange(idStart, idEnd, _colIndexes.size());
    if (retDict == null)
        return new ColGroupEmpty(outputCols);
    if (retDict.getNumberOfValues(outputCols.size()) != getNumValues())
        throw new DMLCompressionException("Invalid Slice Multi Columns");
    return copyAndSet(outputCols, retDict);
}