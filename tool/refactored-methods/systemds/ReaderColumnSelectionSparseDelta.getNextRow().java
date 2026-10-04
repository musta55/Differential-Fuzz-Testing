protected final DblArray getNextRow() {
    _rl++;
    resetReusableArray();
    if (!_a.isEmpty(_rl))
        processInRange(_rl);
    updatePreviousRow();
    return reusableReturn;
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

