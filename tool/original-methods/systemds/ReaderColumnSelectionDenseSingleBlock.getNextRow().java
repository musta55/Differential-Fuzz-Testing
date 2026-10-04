protected DblArray getNextRow() {
    _rl++;
    final int indexOff = _rl * _numCols;
    for (int i = 0; i < _colIndexes.size(); i++) reusableArr[i] = _data[indexOff + _colIndexes.get(i)];
    return reusableReturn;
}