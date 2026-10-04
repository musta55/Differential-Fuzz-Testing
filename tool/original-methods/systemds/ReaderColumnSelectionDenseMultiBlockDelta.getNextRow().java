protected DblArray getNextRow() {
    _rl++;
    if (_isFirstRow) {
        for (int i = 0; i < _colIndexes.size(); i++) {
            final double val = _data.get(_rl, _colIndexes.get(i));
            _previousRow[i] = val;
            reusableArr[i] = val;
        }
        _isFirstRow = false;
    } else {
        for (int i = 0; i < _colIndexes.size(); i++) {
            final double currentVal = _data.get(_rl, _colIndexes.get(i));
            reusableArr[i] = currentVal - _previousRow[i];
            _previousRow[i] = currentVal;
        }
    }
    return reusableReturn;
}