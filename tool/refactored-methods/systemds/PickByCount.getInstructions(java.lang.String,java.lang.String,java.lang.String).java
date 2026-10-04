@Override
public String getInstructions(String input1, String input2, String output) {
    StringBuilder sb = InstructionUtils.getStringBuilder();
    sb.append(getExecType());
    sb.append(Lop.OPERAND_DELIMITOR);
    sb.append(OPCODE);
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(0).prepInputOperand(input1));
    sb.append(OPERAND_DELIMITOR);
    if (operation != OperationTypes.MEDIAN) {
        if (getInputs().get(1).getDataType() == DataType.SCALAR)
            sb.append(getInputs().get(1).prepScalarInputOperand(getExecType()));
        else {
            sb.append(getInputs().get(1).prepInputOperand(input2));
        }
        sb.append(OPERAND_DELIMITOR);
    }
    sb.append(prepOutputOperand(output));
    sb.append(OPERAND_DELIMITOR);
    sb.append(operation);
    sb.append(OPERAND_DELIMITOR);
    sb.append(inMemoryInput);
    if (getExecType() == ExecType.FED) {
        sb.append(OPERAND_DELIMITOR);
        sb.append(_fedOutput.name());
    }
    return sb.toString();
}