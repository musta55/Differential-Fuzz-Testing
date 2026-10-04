final void processInRange(final int r) {
    int apos = _a.pos(r);
    int alen = _a.size(r) + apos;
    int[] aix = _a.indexes(r);
    double[] avals = _a.values(r);
    int skip = 0;
    int j = Arrays.binarySearch(aix, apos, alen, _colIndexes.get(0));
    if (j < 0)
        j = Math.abs(j + 1);
    while (skip < _colIndexes.size() && j < alen) {
        if (_colIndexes.get(skip) == aix[j]) {
            reusableArr[skip] = avals[j];
            skip++;
            j++;
        } else if (_colIndexes.get(skip) > aix[j]) {
            j++;
        } else {
            skip++;
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void resetReusableArray() {
    for (int i = 0; i < _colIndexes.size(); i++) reusableArr[i] = 0.0;
}

private void updatePreviousRow() {
    if (_isFirstRow) {
        System.arraycopy(reusableArr, 0, _previousRow, 0, _colIndexes.size());
        _isFirstRow = false;
    } else {
        for (int i = 0; i < _colIndexes.size(); i++) {
            double currentVal = reusableArr[i];
            reusableArr[i] = currentVal - _previousRow[i];
            _previousRow[i] = currentVal;
        }
    }
}

