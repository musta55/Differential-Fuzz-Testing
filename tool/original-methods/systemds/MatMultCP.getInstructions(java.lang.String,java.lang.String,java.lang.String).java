@Override
public String getInstructions(String input1, String input2, String output) {
    String ret = null;
    if (!useTranspose) {
        ret = InstructionUtils.concatOperands(getExecType().name(), Opcodes.MMULT.toString(), getInputs().get(0).prepInputOperand(input1), getInputs().get(1).prepInputOperand(input2), prepOutputOperand(output), String.valueOf(numThreads));
    } else {
        // GPU or compressed
        ret = InstructionUtils.concatOperands(getExecType().name(), Opcodes.MMULT.toString(), getInputs().get(0).prepInputOperand(input1), getInputs().get(1).prepInputOperand(input2), prepOutputOperand(output), String.valueOf(numThreads), String.valueOf(isLeftTransposed), String.valueOf(isRightTransposed));
    }
    if (getExecType() == ExecType.FED)
        ret = InstructionUtils.concatOperands(ret, _fedOutput.name());
    return ret;
}