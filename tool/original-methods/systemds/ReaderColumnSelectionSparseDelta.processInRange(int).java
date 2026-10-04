final void processInRange(final int r) {
    final int apos = _a.pos(r);
    final int alen = _a.size(r) + apos;
    final int[] aix = _a.indexes(r);
    final double[] avals = _a.values(r);
    int skip = 0;
    int j = Arrays.binarySearch(aix, apos, alen, _colIndexes.get(0));
    if (j < 0)
        j = Math.abs(j + 1);
    while (skip < _colIndexes.size() && j < alen) {
        if (_colIndexes.get(skip) == aix[j]) {
            reusableArr[skip] = avals[j];
            skip++;
            j++;
        } else if (_colIndexes.get(skip) > aix[j])
            j++;
        else
            skip++;
    }
}