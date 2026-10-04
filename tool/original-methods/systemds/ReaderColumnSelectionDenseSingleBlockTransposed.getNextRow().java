protected DblArray getNextRow() {
    _rl++;
    for (int i = 0; i < _colIndexes.size(); i++) reusableArr[i] = _data[_colIndexes.get(i) * _nColIn + _rl];
    return reusableReturn;
}