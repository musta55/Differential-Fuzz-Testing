@Override
public String getInstructions(String lhsInput, String rhsInput, String rowl, String rowu, String coll, String colu, String output) {
    StringBuilder sb = InstructionUtils.getStringBuilder();
    sb.append(getExecType());
    sb.append(OPERAND_DELIMITOR);
    sb.append(getOpcode());
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(0).prepInputOperand(lhsInput));
    sb.append(OPERAND_DELIMITOR);
    if (getInputs().get(1).getDataType() == DataType.SCALAR) {
        sb.append(getInputs().get(1).prepScalarInputOperand(getExecType()));
    } else {
        sb.append(getInputs().get(1).prepInputOperand(rhsInput));
    }
    sb.append(OPERAND_DELIMITOR);
    // rowl, rowu
    sb.append(getInputs().get(2).prepScalarInputOperand(getExecType()));
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(3).prepScalarInputOperand(getExecType()));
    sb.append(OPERAND_DELIMITOR);
    // rowl, rowu
    sb.append(getInputs().get(4).prepScalarInputOperand(getExecType()));
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(5).prepScalarInputOperand(getExecType()));
    sb.append(OPERAND_DELIMITOR);
    sb.append(this.prepOutputOperand(output));
    if (getExecType() == ExecType.SPARK) {
        sb.append(OPERAND_DELIMITOR);
        sb.append(_type.toString());
    }
    return sb.toString();
}