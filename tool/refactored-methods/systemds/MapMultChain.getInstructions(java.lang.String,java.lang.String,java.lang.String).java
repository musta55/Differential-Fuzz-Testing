@Override
public String getInstructions(String input1, String input2, String output) {
    return buildInstructionString(input1, input2, null, output);
}
// ---- helper method(s) introduced by the refactoring ----
private String buildInstructionString(String input1, String input2, String input3, String output) {
    StringBuilder sb = InstructionUtils.getStringBuilder();
    sb.append(getExecType());
    sb.append(Lop.OPERAND_DELIMITOR);
    if (getExecType() == ExecType.CP)
        sb.append(OPCODE_CP);
    else
        sb.append(OPCODE);
    sb.append(Lop.OPERAND_DELIMITOR);
    sb.append(getInputs().get(0).prepInputOperand(input1));
    sb.append(Lop.OPERAND_DELIMITOR);
    sb.append(getInputs().get(1).prepInputOperand(input2));
    if (input3 != null) {
        sb.append(Lop.OPERAND_DELIMITOR);
        sb.append(getInputs().get(2).prepInputOperand(input3));
    }
    sb.append(Lop.OPERAND_DELIMITOR);
    sb.append(prepOutputOperand(output));
    sb.append(Lop.OPERAND_DELIMITOR);
    sb.append(_chainType);
    //append degree of parallelism for matrix multiplications
    if (getExecType() == ExecType.CP) {
        sb.append(OPERAND_DELIMITOR);
        sb.append(_numThreads);
    }
    return sb.toString();
}

