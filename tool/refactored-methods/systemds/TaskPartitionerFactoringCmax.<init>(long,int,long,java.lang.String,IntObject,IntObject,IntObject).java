public TaskPartitionerFactoringCmax(long taskSize, int numThreads, long constraint, String iterVarName, IntObject fromVal, IntObject toVal, IntObject incrVal) {
    super(taskSize, numThreads, iterVarName, fromVal, toVal, incrVal);
    setConstraint(constraint);
}
// ---- helper method(s) introduced by the refactoring ----
private void setConstraint(long constraint) {
    _constraint = constraint;
}

