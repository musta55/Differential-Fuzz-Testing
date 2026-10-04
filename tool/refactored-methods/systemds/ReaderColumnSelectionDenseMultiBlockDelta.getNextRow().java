protected DblArray getNextRow() {
    _rl++;
    if (_isFirstRow) {
        processFirstRow();
    } else {
        processSubsequentRow();
    }
    return reusableReturn;
}
// ---- helper method(s) introduced by the refactoring ----
private void processFirstRow() {
    for (int i = 0; i < _colIndexes.size(); i++) {
        final double val = _data.get(_rl, _colIndexes.get(i));
        _previousRow[i] = val;
        reusableArr[i] = val;
    }
    _isFirstRow = false;
}

private void processSubsequentRow() {
    for (int i = 0; i < _colIndexes.size(); i++) {
        final double currentVal = _data.get(_rl, _colIndexes.get(i));
        reusableArr[i] = currentVal - _previousRow[i];
        _previousRow[i] = currentVal;
    }
}

