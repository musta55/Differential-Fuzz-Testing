protected DblArray getNextRow() {
    _rl++;
    populateReusableArrayWithCurrentRowValues();
    return reusableReturn;
}
// ---- helper method(s) introduced by the refactoring ----
private void populateReusableArrayWithCurrentRowValues() {
    for (int i = 0; i < _colIndexes.size(); i++) reusableArr[i] = _data.get(_colIndexes.get(i), _rl);
}

