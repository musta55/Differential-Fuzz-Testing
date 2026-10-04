@Override
public String getInstructions(String input1, String output) {
    StringBuilder sb = InstructionUtils.getStringBuilder();
    sb.append(getExecType());
    sb.append(Lop.OPERAND_DELIMITOR);
    sb.append(OPCODE);
    sb.append(OPERAND_DELIMITOR);
    Lop input = getInputs().get(0);
    String operand = input instanceof FunctionCallCP && ((FunctionCallCP) input).getFunctionName().equalsIgnoreCase(Opcodes.TRANSFORMENCODE.toString()) ? input.getOutputs().get(0).getOutputParameters().getLabel() : input.prepInputOperand(input1);
    sb.append(operand);
    sb.append(OPERAND_DELIMITOR);
    sb.append(prepOutputOperand(output));
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