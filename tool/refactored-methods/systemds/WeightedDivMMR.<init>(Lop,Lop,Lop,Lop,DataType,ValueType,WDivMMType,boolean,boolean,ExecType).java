public WeightedDivMMR(Lop input1, Lop input2, Lop input3, Lop input4, DataType dt, ValueType vt, WDivMMType wt, boolean cacheU, boolean cacheV, ExecType et) {
    this(input1, input2, input3, input4, dt, vt, wt, cacheU, cacheV);
    setupLopProperties(et);
}
// ---- helper method(s) introduced by the refactoring ----
private WeightedDivMMR(Lop input1, Lop input2, Lop input3, Lop input4, DataType dt, ValueType vt, WDivMMType wt, boolean cacheU, boolean cacheV) {
    super(Lop.Type.WeightedDivMM, dt, vt);
    addInputs(input1, input2, input3, input4);
    setOutputs(input1, input2, input3, input4);
    _weightsType = wt;
    _cacheU = cacheU;
    _cacheV = cacheV;
}

private void addInputs(Lop... inputs) {
    for (Lop input : inputs) {
        addInput(input);
        input.addOutput(this);
    }
}

private void setOutputs(Lop... inputs) {
    for (Lop input : inputs) {
        input.addOutput(this);
    }
}

