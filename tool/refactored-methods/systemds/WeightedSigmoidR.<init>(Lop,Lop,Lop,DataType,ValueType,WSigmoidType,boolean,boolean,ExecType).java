public WeightedSigmoidR(Lop input1, Lop input2, Lop input3, DataType dt, ValueType vt, WSigmoidType wt, boolean cacheU, boolean cacheV, ExecType et) {
    super(Lop.Type.WeightedSigmoid, dt, vt);
    initializeInputs(input1, input2, input3);
    setParameters(wt, cacheU, cacheV);
    setupLopProperties(et);
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeInputs(Lop input1, Lop input2, Lop input3) {
    //X
    addInput(input1);
    //U
    addInput(input2);
    //V
    addInput(input3);
    input1.addOutput(this);
    input2.addOutput(this);
    input3.addOutput(this);
}

private void setParameters(WSigmoidType wt, boolean cacheU, boolean cacheV) {
    _wsType = wt;
    _cacheU = cacheU;
    _cacheV = cacheV;
}

