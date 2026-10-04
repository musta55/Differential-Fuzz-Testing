public ReBlock(Lop input, int blen, DataType dt, ValueType vt, boolean outputEmptyBlocks, ExecType et) {
    super(Lop.Type.ReBlock, dt, vt);
    addInput(input);
    input.addOutput(this);
    _blocksize = blen;
    _outputEmptyBlocks = outputEmptyBlocks;
    if (et == ExecType.SPARK || et == ExecType.OOC)
        lps.setProperties(inputs, et);
    else
        throw new LopsException("Incorrect execution type for Reblock:" + et);
}