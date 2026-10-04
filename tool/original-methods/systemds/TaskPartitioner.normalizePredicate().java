/**
 * Normalizes the (from, to, incr) predicate to a predicate w/
 * positive increment.
 */
private void normalizePredicate() {
    //check for positive increment
    if (_incrVal.getLongValue() >= 0)
        return;
    long lfrom = _fromVal.getLongValue();
    long lto = _toVal.getLongValue();
    long lincr = _incrVal.getLongValue();
    _fromVal = new IntObject(lfrom - ((lfrom - lto) / lincr * lincr));
    _toVal = new IntObject(lfrom);
    _incrVal = new IntObject(-1 * lincr);
}