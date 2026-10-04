protected DblArray getNextRow() {
    _rl++;
    populateReusableArray();
    return reusableReturn;
}
// ---- helper method(s) introduced by the refactoring ----
private void populateReusableArray() {
    for (int i = 0; i < _colIndexes.size(); i++) reusableArr[i] = _data[_colIndexes.get(i) * _nColIn + _rl];
}

