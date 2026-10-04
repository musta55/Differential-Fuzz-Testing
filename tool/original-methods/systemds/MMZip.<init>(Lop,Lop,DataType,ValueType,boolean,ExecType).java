public MMZip(Lop input1, Lop input2, DataType dt, ValueType vt, boolean tRewrite, ExecType et) {
    //handle inputs and outputs
    super(Lop.Type.MMRJ, dt, vt);
    _tRewrite = tRewrite;
    addInput(input1);
    addInput(input2);
    input1.addOutput(this);
    input2.addOutput(this);
    lps.setProperties(inputs, et);
}