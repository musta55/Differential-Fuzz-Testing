protected DblArray getNextRow() {
    _rl++;
    final int indexOff = _rl * _numCols;
    double scaleFactor = _scaleFactors.length == 1 ? _scaleFactors[0] : _scaleFactors[_rl];
    for (int i = 0; i < _colIndexes.size(); i++) reusableArr[i] = Math.floor(_data[indexOff + _colIndexes.get(i)] * scaleFactor);
    return reusableReturn;
}