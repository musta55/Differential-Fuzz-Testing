public RemoteParForJobReturn(boolean isSuccessful, int numberOfIterations, int numberOfTasks, LocalVariableMap[] variables, Lineage[] lineages) {
    _successful = isSuccessful;
    _numTasks = numberOfTasks;
    _numIterations = numberOfIterations;
    _variables = variables;
    _lineages = lineages;
}