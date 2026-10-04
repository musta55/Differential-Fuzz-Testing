public CPOperand() {
    this("", ValueType.UNKNOWN, DataType.UNKNOWN, false);
}
// ---- helper method(s) introduced by the refactoring ----
private void setFields(String name, DataType dataType, ValueType valueType, boolean isLiteral) {
    _name = name;
    _dataType = dataType;
    _valueType = valueType;
    _isLiteral = isLiteral;
}

