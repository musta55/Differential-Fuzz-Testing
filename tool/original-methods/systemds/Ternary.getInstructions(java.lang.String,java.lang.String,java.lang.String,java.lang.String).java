@Override
public String getInstructions(String input1, String input2, String input3, String output) {
    String ret = InstructionUtils.concatOperands(getExecType().name(), _op.toString(), getInputs().get(0).prepInputOperand(input1), getInputs().get(1).prepInputOperand(input2), getInputs().get(2).prepInputOperand(input3), prepOutputOperand(output));
    if (getDataType().isMatrix()) {
        if (getExecType() == ExecType.CP)
            ret = InstructionUtils.concatOperands(ret, String.valueOf(_numThreads));
        else if (getExecType() == ExecType.FED)
            ret = InstructionUtils.concatOperands(ret, String.valueOf(_numThreads), _fedOutput.name());
    }
    return ret;
}