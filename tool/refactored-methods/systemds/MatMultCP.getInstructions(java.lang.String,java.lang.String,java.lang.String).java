@Override
public String getInstructions(String input1, String input2, String output) {
    StringBuilder sb = new StringBuilder();
    sb.append(getExecType().name()).append(" ");
    sb.append(Opcodes.MMULT).append(" ");
    sb.append(getInputs().get(0).prepInputOperand(input1)).append(" ");
    sb.append(getInputs().get(1).prepInputOperand(input2)).append(" ");
    sb.append(prepOutputOperand(output)).append(" ");
    sb.append(numThreads);
    if (useTranspose) {
        sb.append(" ").append(isLeftTransposed);
        sb.append(" ").append(isRightTransposed);
    }
    if (getExecType() == ExecType.FED) {
        sb.append(" ").append(_fedOutput.name());
    }
    return sb.toString();
}