public Sql(HashMap<String, Lop> inputParametersLops, DataType dt, ValueType vt) {
    super(Lop.Type.Sql, dt, vt);
    _inputParams = inputParametersLops;
    addInputAndOutput(SQL_CONN);
    addInputAndOutput(SQL_USER);
    addInputAndOutput(SQL_PASS);
    addInputAndOutput(SQL_QUERY);
}
// ---- helper method(s) introduced by the refactoring ----
private void addInputAndOutput(String key) {
    Lop lop = _inputParams.get(key);
    addInput(lop);
    lop.addOutput(this);
}

private String prepOperand(String input, String key) {
    Lop inLop = _inputParams.get(key);
    boolean literal = (inLop instanceof Data && ((Data) inLop).isLiteral());
    return prepOperand(input, DataType.SCALAR, ValueType.STRING, literal);
}

