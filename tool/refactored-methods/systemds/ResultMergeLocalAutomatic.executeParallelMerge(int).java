@Override
public MatrixObject executeParallelMerge(int par) {
    DataCharacteristics dc = _output.getDataCharacteristics();
    long rows = dc.getRows();
    long cols = dc.getCols();
    initializeResultMerge(par * rows, cols, par);
    return _rm.executeParallelMerge(par);
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeResultMerge(long rows, long cols, int par) {
    if (OptimizerRuleBased.isInMemoryResultMerge(rows, cols, OptimizerUtils.getLocalMemBudget()))
        _rm = new ResultMergeLocalMemory(_output, _inputs, _outputFName, _isAccum);
    else
        _rm = new ResultMergeLocalFile(_output, _inputs, _outputFName, _isAccum);
}

