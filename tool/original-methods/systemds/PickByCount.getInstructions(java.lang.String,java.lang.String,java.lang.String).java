/*
	 * This version of getInstructions() must be called only for valuepick (CP), IQM (CP)
	 * 
	 * Example instances:
	 * valuepick:::temp2:STRING:::0.25:DOUBLE:::Var1:DOUBLE
	 * valuepick:::temp2:STRING:::Var1:DOUBLE:::Var2:DOUBLE
	 */
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
    sb.append(this.prepOutputOperand(output));
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