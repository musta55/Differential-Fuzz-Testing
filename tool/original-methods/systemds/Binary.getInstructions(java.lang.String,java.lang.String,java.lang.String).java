@Override
public String getInstructions(String input1, String input2, String output) {
    String ret = InstructionUtils.concatOperands(getExecType().name(), getOpcode(), getInputs().get(0).prepInputOperand(input1), getInputs().get(1).prepInputOperand(input2), prepOutputOperand(output));
    if (getExecType() == ExecType.CP)
        ret = InstructionUtils.concatOperands(ret, String.valueOf(_numThreads));
    else if (getExecType() == ExecType.FED)
        ret = InstructionUtils.concatOperands(ret, String.valueOf(_numThreads), _fedOutput.name());
    if (getExecType() == ExecType.CP && inplace)
        ret = InstructionUtils.concatOperands(ret, "InPlace");
    return ret;
}