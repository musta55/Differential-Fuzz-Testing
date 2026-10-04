public void split(String str) {
    String[] opr = str.split(Instruction.VALUETYPE_PREFIX);
    if (opr.length == 4) {
        _name = opr[0];
        _dataType = DataType.valueOf(opr[1]);
        _valueType = ValueType.valueOf(opr[2]);
        _isLiteral = Boolean.parseBoolean(opr[3]);
    } else if (opr.length == 3) {
        _name = opr[0];
        _dataType = DataType.valueOf(opr[1]);
        _valueType = ValueType.valueOf(opr[2]);
        _isLiteral = false;
    } else if (opr.length == 1) {
        //note: for literals in MR instructions
        _name = opr[0];
        _dataType = DataType.SCALAR;
        _valueType = ValueType.FP64;
        _isLiteral = true;
    } else {
        _name = opr[0];
        _valueType = ValueType.valueOf(opr[1]);
    }
}