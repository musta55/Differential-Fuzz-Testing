public CPOperand(String str) {
    this("", ValueType.UNKNOWN, DataType.UNKNOWN);
    split(str);
}