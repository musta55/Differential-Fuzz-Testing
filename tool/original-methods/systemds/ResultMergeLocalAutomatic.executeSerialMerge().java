@Override
public MatrixObject executeSerialMerge() {
    Timing time = new Timing(true);
    DataCharacteristics dc = _output.getDataCharacteristics();
    long rows = dc.getRows();
    long cols = dc.getCols();
    if (OptimizerRuleBased.isInMemoryResultMerge(rows, cols, OptimizerUtils.getLocalMemBudget()))
        _rm = new ResultMergeLocalMemory(_output, _inputs, _outputFName, _isAccum);
    else
        _rm = new ResultMergeLocalFile(_output, _inputs, _outputFName, _isAccum);
    MatrixObject ret = _rm.executeSerialMerge();
    LOG.trace("Automatic result merge (" + _rm.getClass().getName() + ") executed in " + time.stop() + "ms.");
    return ret;
}