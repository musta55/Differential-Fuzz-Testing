protected DblArray getNextRow() {
    _rl++;
    populateReusableArray(_rl * _numCols);
    return reusableReturn;
}
// ---- helper method(s) introduced by the refactoring ----
private void populateReusableArray(int indexOff) {
    for (int i = 0; i < _colIndexes.size(); i++) reusableArr[i] = _data[indexOff + _colIndexes.get(i)];
}

