protected final DblArray getNextRow() {
    _rl++;
    for (int i = 0; i < _colIndexes.size(); i++) reusableArr[i] = 0.0;
    if (!_a.isEmpty(_rl))
        processInRange(_rl);
    if (_isFirstRow) {
        for (int i = 0; i < _colIndexes.size(); i++) _previousRow[i] = reusableArr[i];
        _isFirstRow = false;
    } else {
        for (int i = 0; i < _colIndexes.size(); i++) {
            final double currentVal = reusableArr[i];
            reusableArr[i] = currentVal - _previousRow[i];
            _previousRow[i] = currentVal;
        }
    }
    return reusableReturn;
}