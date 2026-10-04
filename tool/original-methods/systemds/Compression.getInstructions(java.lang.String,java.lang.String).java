@Override
public String getInstructions(String input1, String output) {
    StringBuilder sb = InstructionUtils.getStringBuilder();
    sb.append(getExecType());
    sb.append(Lop.OPERAND_DELIMITOR);
    sb.append(OPCODE);
    sb.append(OPERAND_DELIMITOR);
    if (getInputs().get(0) instanceof FunctionCallCP && ((FunctionCallCP) getInputs().get(0)).getFunctionName().equalsIgnoreCase(Opcodes.TRANSFORMENCODE.toString())) {
        sb.append(getInputs().get(0).getOutputs().get(0).getOutputParameters().getLabel());
    } else {
        sb.append(getInputs().get(0).prepInputOperand(input1));
    }
    sb.append(OPERAND_DELIMITOR);
    if (getInputs().get(0) instanceof FunctionCallCP && ((FunctionCallCP) getInputs().get(0)).getFunctionName().equalsIgnoreCase(Opcodes.TRANSFORMENCODE.toString())) {
        sb.append(getInputs().get(0).getOutputs().get(0).getOutputParameters().getLabel());
    } else {
        sb.append(prepOutputOperand(output));
    }
    if (_singletonLookupKey != 0) {
        sb.append(OPERAND_DELIMITOR);
        sb.append(_singletonLookupKey);
    }
    if (getExecType().equals(ExecType.CP) || getExecType().equals(ExecType.FED)) {
        sb.append(OPERAND_DELIMITOR);
        sb.append(_numThreads);
    }
    return sb.toString();
}