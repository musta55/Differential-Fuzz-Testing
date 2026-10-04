public TernaryAggregate(Lop input1, Lop input2, Lop input3, AggOp aggOp, OpOp2 binOp, Direction direction, DataType dt, ValueType vt, ExecType et, int k) {
    this(new Builder().input1(input1).input2(input2).input3(input3).direction(direction).dataType(dt).valueType(vt).execType(et).numThreads(k));
}
// ---- helper method(s) introduced by the refactoring ----
private TernaryAggregate(Builder builder) {
    super(Lop.Type.TernaryAggregate, builder.dataType, builder.valueType);
    addInput(builder.input1);
    addInput(builder.input2);
    addInput(builder.input3);
    builder.input1.addOutput(this);
    builder.input2.addOutput(this);
    builder.input3.addOutput(this);
    _direction = builder.direction;
    _numThreads = builder.numThreads;
    lps.setProperties(inputs, builder.execType);
}

private void appendNumThreads(StringBuilder sb) {
    if (getExecType() == ExecType.CP || getExecType() == ExecType.FED) {
        sb.append(OPERAND_DELIMITOR);
        sb.append(_numThreads);
    }
}

private void appendFedOutput(StringBuilder sb) {
    if (getExecType() == ExecType.FED) {
        sb.append(OPERAND_DELIMITOR);
        sb.append(_fedOutput.name());
    }
}

