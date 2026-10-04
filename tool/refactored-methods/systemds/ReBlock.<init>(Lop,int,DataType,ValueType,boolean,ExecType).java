public ReBlock(Lop input, int blen, DataType dt, ValueType vt, boolean outputEmptyBlocks, ExecType et) {
    super(Lop.Type.ReBlock, dt, vt);
    addInput(input);
    input.addOutput(this);
    setBlockSize(blen);
    setOutputEmptyBlocks(outputEmptyBlocks);
    setExecutionProperties(et);
}
// ---- helper method(s) introduced by the refactoring ----
private void setBlockSize(int blen) {
    _blocksize = blen;
}

private void setOutputEmptyBlocks(boolean outputEmptyBlocks) {
    _outputEmptyBlocks = outputEmptyBlocks;
}

private void setExecutionProperties(ExecType et) {
    if (et == ExecType.SPARK || et == ExecType.OOC)
        lps.setProperties(inputs, et);
    else
        throw new LopsException("Incorrect execution type for Reblock:" + et);
}

