public void split(String str) {
    String[] parts = str.split(Instruction.VALUETYPE_PREFIX);
    int length = parts.length;
    if (length == 4) {
        setFields(parts[0], DataType.valueOf(parts[1]), ValueType.valueOf(parts[2]), Boolean.parseBoolean(parts[3]));
    } else if (length == 3) {
        setFields(parts[0], DataType.valueOf(parts[1]), ValueType.valueOf(parts[2]), false);
    } else if (length == 1) {
        setFields(parts[0], DataType.SCALAR, ValueType.FP64, true);
    } else {
        setFields(parts[0], null, ValueType.valueOf(parts[1]), false);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void setFields(String name, DataType dataType, ValueType valueType, boolean isLiteral) {
    _name = name;
    _dataType = dataType;
    _valueType = valueType;
    _isLiteral = isLiteral;
}

