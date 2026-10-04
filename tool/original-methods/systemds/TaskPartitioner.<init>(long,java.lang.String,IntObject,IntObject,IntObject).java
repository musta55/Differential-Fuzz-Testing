protected TaskPartitioner(long taskSize, String iterVarName, IntObject fromVal, IntObject toVal, IntObject incrVal) {
    _taskSize = taskSize;
    _iterVarName = iterVarName;
    _fromVal = fromVal;
    _toVal = toVal;
    _incrVal = incrVal;
    //normalize predicate if necessary
    normalizePredicate();
    //compute number of iterations
    _numIter = (long) Math.ceil(((double) (_toVal.getLongValue() - _fromVal.getLongValue() + 1)) / _incrVal.getLongValue());
}