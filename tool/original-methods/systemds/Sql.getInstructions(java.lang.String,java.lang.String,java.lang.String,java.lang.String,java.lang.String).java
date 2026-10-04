@Override
public String getInstructions(String input1, String input2, String input3, String input4, String output) {
    StringBuilder sb = InstructionUtils.getStringBuilder();
    // TODO spark
    sb.append("CP");
    sb.append(OPERAND_DELIMITOR);
    sb.append(Opcodes.SQL);
    sb.append(OPERAND_DELIMITOR);
    Lop inLop = _inputParams.get(SQL_CONN);
    boolean literal = (inLop instanceof Data && ((Data) inLop).isLiteral());
    sb.append(prepOperand(input1, DataType.SCALAR, ValueType.STRING, literal));
    sb.append(OPERAND_DELIMITOR);
    inLop = _inputParams.get(DataExpression.SQL_USER);
    literal = (inLop instanceof Data && ((Data) inLop).isLiteral());
    sb.append(prepOperand(input2, DataType.SCALAR, ValueType.STRING, literal));
    sb.append(OPERAND_DELIMITOR);
    inLop = _inputParams.get(DataExpression.SQL_PASS);
    literal = (inLop instanceof Data && ((Data) inLop).isLiteral());
    sb.append(prepOperand(input3, DataType.SCALAR, ValueType.STRING, literal));
    sb.append(OPERAND_DELIMITOR);
    inLop = _inputParams.get(DataExpression.SQL_QUERY);
    literal = (inLop instanceof Data && ((Data) inLop).isLiteral());
    sb.append(prepOperand(input4, DataType.SCALAR, ValueType.STRING, literal));
    sb.append(OPERAND_DELIMITOR);
    sb.append(prepOutputOperand(output));
    return sb.toString();
}