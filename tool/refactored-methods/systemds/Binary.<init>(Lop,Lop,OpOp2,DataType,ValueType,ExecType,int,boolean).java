public Binary(Lop input1, Lop input2, OpOp2 op, DataType dt, ValueType vt, ExecType et, int k, boolean inplace) {
    super(Lop.Type.Binary, dt, vt);
    operation = op;
    _numThreads = k;
    this.inplace = inplace;
    addInput(input1);
    addInput(input2);
    input1.addOutput(this);
    input2.addOutput(this);
    lps.setProperties(inputs, et);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isSparkExecution() {
    return getExecType() == ExecType.SPARK;
}

private boolean isFrameMatrixCombination(ArrayList<Lop> inputs) {
    return inputs.get(0).getDataType() == DataType.FRAME && inputs.get(1).getDataType() == DataType.MATRIX;
}

private boolean isCPExecution() {
    return getExecType() == ExecType.CP;
}

private boolean isFedExecution() {
    return getExecType() == ExecType.FED;
}

