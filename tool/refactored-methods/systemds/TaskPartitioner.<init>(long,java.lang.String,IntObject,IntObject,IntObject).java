protected TaskPartitioner(long taskSize, String iterVarName, IntObject fromVal, IntObject toVal, IntObject incrVal) {
    initializeTaskSize(taskSize);
    setIterationVariableName(iterVarName);
    setFromValue(fromVal);
    setToValue(toVal);
    setIncrementValue(incrVal);
    normalizePredicateIfNecessary();
    calculateNumberOfIterations();
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

