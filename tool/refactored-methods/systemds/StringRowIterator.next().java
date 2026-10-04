@Override
public String[] next() {
    populateCurrentRow();
    _curPos++;
    return _curRow;
}
// ---- helper method(s) introduced by the refactoring ----
private void populateCurrentRow() {
    for (int j = 0; j < _cols.length; j++) {
        Object tmp = _fb.get(_curPos, _cols[j] - 1);
        _curRow[j] = (tmp != null) ? tmp.toString() : null;
    }
}

