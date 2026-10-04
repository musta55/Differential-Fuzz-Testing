@Override
public String getInstructions(String lhsInput, String rhsInput, String rowl, String rowu, String coll, String colu, String output) {
    StringBuilder sb = InstructionUtils.getStringBuilder();
    sb.append(getExecType()).append(OPERAND_DELIMITOR);
    sb.append(getOpcode()).append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(0).prepInputOperand(lhsInput)).append(OPERAND_DELIMITOR);
    if (getInputs().get(1).getDataType() == DataType.SCALAR) {
        sb.append(getInputs().get(1).prepScalarInputOperand(getExecType()));
    } else {
        sb.append(getInputs().get(1).prepInputOperand(rhsInput));
    }
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(2).prepScalarInputOperand(getExecType())).append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(3).prepScalarInputOperand(getExecType())).append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(4).prepScalarInputOperand(getExecType())).append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(5).prepScalarInputOperand(getExecType())).append(OPERAND_DELIMITOR);
    sb.append(this.prepOutputOperand(output));
    if (getExecType() == ExecType.SPARK) {
        sb.append(OPERAND_DELIMITOR).append(_type.toString());
    }
    return sb.toString();
}