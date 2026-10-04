public RemoteParForJobReturn(boolean successful, int numTasks, int numIters, LocalVariableMap[] variables, Lineage[] lineages) {
    _successful = successful;
    _numTasks = numTasks;
    _numIters = numIters;
    _variables = variables;
    _lineages = lineages;
}