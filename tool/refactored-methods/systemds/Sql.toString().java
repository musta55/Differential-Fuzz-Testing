@Override
public String toString() {
    // TODO Sql.toString() lop
    return "Sql: " + _inputParams.toString();
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

