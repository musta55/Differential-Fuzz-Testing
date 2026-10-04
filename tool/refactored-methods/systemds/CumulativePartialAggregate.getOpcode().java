private String getOpcode() {
    return OPCODE_MAP.get(_op);
}
// ---- helper method(s) introduced by the refactoring ----
private CumulativePartialAggregate(DataType dt, ValueType vt, AggOp op, ExecType et) {
    super(Lop.Type.CumulativePartialAggregate, dt, vt);
    if (!isSupportedAggregate(op)) {
        throw new LopsException("Unsupported aggregate operation type: " + op);
    }
    _op = op;
    lps.setProperties(inputs, et);
}

private boolean isSupportedAggregate(AggOp op) {
    return OPCODE_MAP.containsKey(op);
}

