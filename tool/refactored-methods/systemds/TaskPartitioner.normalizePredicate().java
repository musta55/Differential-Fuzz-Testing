private void normalizePredicate() {
    long fromValue = _fromVal.getLongValue();
    long toValue = _toVal.getLongValue();
    long incrementValue = _incrVal.getLongValue();
    _fromVal = new IntObject(fromValue - ((fromValue - toValue) / incrementValue * incrementValue));
    _toVal = new IntObject(fromValue);
    _incrVal = new IntObject(-incrementValue);
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeTaskSize(long taskSize) {
    _taskSize = taskSize;
}

private void setIterationVariableName(String iterVarName) {
    _iterVarName = iterVarName;
}

private void setFromValue(IntObject fromVal) {
    _fromVal = fromVal;
}

private void setToValue(IntObject toVal) {
    _toVal = toVal;
}

private void setIncrementValue(IntObject incrVal) {
    _incrVal = incrVal;
}

private void normalizePredicateIfNecessary() {
    if (_incrVal.getLongValue() < 0) {
        normalizePredicate();
    }
}

private void calculateNumberOfIterations() {
    _numIter = (long) Math.ceil(((double) (_toVal.getLongValue() - _fromVal.getLongValue() + 1)) / _incrVal.getLongValue());
}

