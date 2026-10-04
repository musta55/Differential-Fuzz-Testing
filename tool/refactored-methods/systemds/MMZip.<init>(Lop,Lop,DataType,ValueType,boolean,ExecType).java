public MMZip(Lop input1, Lop input2, DataType dt, ValueType vt, boolean tRewrite, ExecType et) {
    super(Lop.Type.MMRJ, dt, vt);
    setTRewrite(tRewrite);
    addInputsAndOutputs(input1, input2);
    setProperties(et);
}
// ---- helper method(s) introduced by the refactoring ----
private void setTRewrite(boolean tRewrite) {
    _tRewrite = tRewrite;
}

private void addInputsAndOutputs(Lop input1, Lop input2) {
    addInput(input1);
    addInput(input2);
    input1.addOutput(this);
    input2.addOutput(this);
}

private void setProperties(ExecType et) {
    lps.setProperties(inputs, et);
}

