/**
 * Returns the next token in the string as a String.
 *
 * @return next token in the string as a String
 */
public String nextToken() {
    int len = _string.length();
    int start = _pos;
    //find start (skip over leading delimiters)
    while (start < len && _del == _string.charAt(start)) start++;
    //find end (next delimiter) and return
    if (start < len) {
        _pos = _string.indexOf(_del, start);
        if (start < _pos && _pos < len)
            return _string.substring(start, _pos);
        else
            return _string.substring(start);
    }
    //no next token
    throw new NoSuchElementException();
}