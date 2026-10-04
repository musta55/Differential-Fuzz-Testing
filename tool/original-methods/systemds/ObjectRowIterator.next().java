@Override
public Object[] next() {
    for (int j = 0; j < _cols.length; j++) _curRow[j] = getValue(_curPos, _cols[j] - 1);
    _curPos++;
    return _curRow;
}