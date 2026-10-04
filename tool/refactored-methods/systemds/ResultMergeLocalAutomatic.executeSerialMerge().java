@Override
public MatrixObject executeSerialMerge() {
    Timing time = new Timing(true);
    DataCharacteristics dc = _output.getDataCharacteristics();
    long rows = dc.getRows();
    long cols = dc.getCols();
    initializeResultMerge(rows, cols, 1);
    MatrixObject ret = _rm.executeSerialMerge();
    LOG.trace("Automatic result merge (" + _rm.getClass().getName() + ") executed in " + time.stop() + "ms.");
    return ret;
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeResultMerge(long rows, long cols, int par) {
    if (OptimizerRuleBased.isInMemoryResultMerge(rows, cols, OptimizerUtils.getLocalMemBudget()))
        _rm = new ResultMergeLocalMemory(_output, _inputs, _outputFName, _isAccum);
    else
        _rm = new ResultMergeLocalFile(_output, _inputs, _outputFName, _isAccum);
}

