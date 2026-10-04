/**
 * Returns the next token in the string as a String.
 *
 * @return next token in the string as a String
 */
public String nextToken() {
    int len = _string.length();
    int start = findStart(len);
    //find end (next delimiter) and return
    if (start < len) {
        _pos = findEnd(start, len);
        if (start < _pos && _pos < len)
            return _string.substring(start, _pos);
        else
            return _string.substring(start);
    }
    //no next token
    throw new NoSuchElementException();
}
// ---- helper method(s) introduced by the refactoring ----
private int findStart(int len) {
    int start = _pos;
    while (start < len && _del == _string.charAt(start)) start++;
    return start;
}

private int findEnd(int start, int len) {
    return _string.indexOf(_del, start);
}

