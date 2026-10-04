@Override
public String getInstructions(String input1, String input2, String input3, String output) {
    StringBuilder sb = new StringBuilder();
    sb.append(InstructionUtils.concatOperands(getExecType().name(), _op.toString(), getInputs().get(0).prepInputOperand(input1), getInputs().get(1).prepInputOperand(input2), getInputs().get(2).prepInputOperand(input3), prepOutputOperand(output)));
    if (getDataType().isMatrix()) {
        sb.append(InstructionUtils.concatOperands(String.valueOf(_numThreads)));
        if (getExecType() == ExecType.FED)
            sb.append(InstructionUtils.concatOperands(_fedOutput.name()));
    }
    return sb.toString();
}