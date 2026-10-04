protected DblArray getNextRow() {
    _rl++;
    for (int i = 0; i < _colIndexes.size(); i++) reusableArr[i] = _data.get(_colIndexes.get(i), _rl);
    return reusableReturn;
}