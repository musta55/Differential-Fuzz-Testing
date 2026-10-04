protected DblArray getNextRow() {
    _rl++;
    final int indexOff = _rl * _numCols;
    if (_isFirstRow) {
        for (int i = 0; i < _colIndexes.size(); i++) {
            final double val = _data[indexOff + _colIndexes.get(i)];
            _previousRow[i] = val;
            reusableArr[i] = val;
        }
        _isFirstRow = false;
    } else {
        for (int i = 0; i < _colIndexes.size(); i++) {
            final double currentVal = _data[indexOff + _colIndexes.get(i)];
            reusableArr[i] = currentVal - _previousRow[i];
            _previousRow[i] = currentVal;
        }
    }
    return reusableReturn;
}