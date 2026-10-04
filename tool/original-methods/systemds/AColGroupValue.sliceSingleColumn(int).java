@Override
protected AColGroup sliceSingleColumn(int idx) {
    final IColIndex retIndexes = ColIndexFactory.create(1);
    if (_colIndexes.size() == 1)
        return copyAndSet(retIndexes, _dict);
    final IDictionary retDict = _dict.sliceOutColumnRange(idx, idx + 1, _colIndexes.size());
    if (retDict == null)
        return new ColGroupEmpty(retIndexes);
    else
        return copyAndSet(retIndexes, retDict);
}