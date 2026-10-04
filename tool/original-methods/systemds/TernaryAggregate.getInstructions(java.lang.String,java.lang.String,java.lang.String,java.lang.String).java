@Override
public String getInstructions(String input1, String input2, String input3, String output) {
    StringBuilder sb = InstructionUtils.getStringBuilder();
    sb.append(getExecType());
    sb.append(OPERAND_DELIMITOR);
    sb.append(getOpCode());
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(0).prepInputOperand(input1));
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(1).prepInputOperand(input2));
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(2).prepInputOperand(input3));
    sb.append(OPERAND_DELIMITOR);
    sb.append(prepOutputOperand(output));
    if (getExecType() == ExecType.CP || getExecType() == ExecType.FED) {
        sb.append(OPERAND_DELIMITOR);
        sb.append(_numThreads);
        if (getExecType() == ExecType.FED) {
            sb.append(OPERAND_DELIMITOR);
            sb.append(_fedOutput.name());
        }
    }
    return sb.toString();
}