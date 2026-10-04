@Override
public String getInstructions(String input1, String input2, String input3, String input4, String output) {
    StringBuilder sb = InstructionUtils.getStringBuilder();
    // TODO spark
    sb.append("CP");
    sb.append(OPERAND_DELIMITOR);
    sb.append(Opcodes.SQL);
    sb.append(OPERAND_DELIMITOR);
    sb.append(prepOperand(input1, SQL_CONN));
    sb.append(OPERAND_DELIMITOR);
    sb.append(prepOperand(input2, SQL_USER));
    sb.append(OPERAND_DELIMITOR);
    sb.append(prepOperand(input3, SQL_PASS));
    sb.append(OPERAND_DELIMITOR);
    sb.append(prepOperand(input4, SQL_QUERY));
    sb.append(OPERAND_DELIMITOR);
    sb.append(prepOutputOperand(output));
    return sb.toString();
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

