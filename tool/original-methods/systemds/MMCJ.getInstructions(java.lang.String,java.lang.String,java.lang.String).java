//SPARK instruction generation
@Override
public String getInstructions(String input1, String input2, String output) {
    StringBuilder sb = InstructionUtils.getStringBuilder();
    sb.append(getExecType());
    sb.append(Lop.OPERAND_DELIMITOR);
    sb.append("cpmm");
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(0).prepInputOperand(input1));
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(1).prepInputOperand(input2));
    sb.append(OPERAND_DELIMITOR);
    sb.append(prepOutputOperand(output));
    sb.append(OPERAND_DELIMITOR);
    if (getExecType() == ExecType.SPARK) {
        sb.append(_outputEmptyBlocks);
        sb.append(Lop.OPERAND_DELIMITOR);
        sb.append(_aggtype.name());
    } else
        sb.append(_type.name());
    return sb.toString();
}