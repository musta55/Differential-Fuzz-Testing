protected DblArray getNextRow() {
    _rl++;
    for (int i = 0; i < _colIndexes.size(); i++) reusableArr[i] = _data.get(_rl, _colIndexes.get(i));
    return reusableReturn;
}