protected DblArray getNextRow() {
    _rl++;
    populateReusableArrWithRowData();
    return reusableReturn;
}
// ---- helper method(s) introduced by the refactoring ----
private void populateReusableArrWithRowData() {
    for (int i = 0; i < _colIndexes.size(); i++) reusableArr[i] = _data.get(_rl, _colIndexes.get(i));
}

