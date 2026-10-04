@Override
public MatrixObject executeParallelMerge(int par) {
    DataCharacteristics dc = _output.getDataCharacteristics();
    long rows = dc.getRows();
    long cols = dc.getCols();
    if (OptimizerRuleBased.isInMemoryResultMerge(par * rows, cols, OptimizerUtils.getLocalMemBudget()))
        _rm = new ResultMergeLocalMemory(_output, _inputs, _outputFName, _isAccum);
    else
        _rm = new ResultMergeLocalFile(_output, _inputs, _outputFName, _isAccum);
    return _rm.executeParallelMerge(par);
}