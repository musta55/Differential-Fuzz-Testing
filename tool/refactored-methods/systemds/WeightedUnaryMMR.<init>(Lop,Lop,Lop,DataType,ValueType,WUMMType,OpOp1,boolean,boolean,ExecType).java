public WeightedUnaryMMR(Lop input1, Lop input2, Lop input3, DataType dt, ValueType vt, WUMMType wt, OpOp1 op, boolean cacheU, boolean cacheV, ExecType et) {
    super(Lop.Type.WeightedUMM, dt, vt);
    addInputsAndOutputs(input1, input2, input3);
    setupMapMultParameters(wt, op, cacheU, cacheV);
    setupLopProperties(et);
}
// ---- helper method(s) introduced by the refactoring ----
private void addInputsAndOutputs(Lop input1, Lop input2, Lop input3) {
    // X
    addInput(input1);
    // U
    addInput(input2);
    // V
    addInput(input3);
    input1.addOutput(this);
    input2.addOutput(this);
    input3.addOutput(this);
}

private void setupMapMultParameters(WUMMType wt, OpOp1 op, boolean cacheU, boolean cacheV) {
    _wummType = wt;
    _uop = op;
    _cacheU = cacheU;
    _cacheV = cacheV;
}

