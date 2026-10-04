@Override
public String getInstructions(String input1, String input2, String output) {
    String ret = InstructionUtils.concatOperands(getExecType().name(), getOpcode(), getInputs().get(0).prepInputOperand(input1), getInputs().get(1).prepInputOperand(input2), prepOutputOperand(output));
    if (isCPExecution())
        ret = InstructionUtils.concatOperands(ret, String.valueOf(_numThreads));
    else if (isFedExecution())
        ret = InstructionUtils.concatOperands(ret, String.valueOf(_numThreads), _fedOutput.name());
    if (isCPExecution() && inplace)
        ret = InstructionUtils.concatOperands(ret, "InPlace");
    return ret;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isSparkExecution() {
    return getExecType() == ExecType.SPARK;
}

private boolean isFrameMatrixCombination(ArrayList<Lop> inputs) {
    return inputs.get(0).getDataType() == DataType.FRAME && inputs.get(1).getDataType() == DataType.MATRIX;
}

private boolean isCPExecution() {
    return getExecType() == ExecType.CP;
}

private boolean isFedExecution() {
    return getExecType() == ExecType.FED;
}

