@Override
public String[] next() {
    for (int j = 0; j < _cols.length; j++) {
        Object tmp = _fb.get(_curPos, _cols[j] - 1);
        _curRow[j] = (tmp != null) ? tmp.toString() : null;
    }
    _curPos++;
    return _curRow;
}