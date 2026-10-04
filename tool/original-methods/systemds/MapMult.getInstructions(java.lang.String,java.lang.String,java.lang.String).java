@Override
public String getInstructions(String input1, String input2, String output) {
    String ret = InstructionUtils.concatOperands(getExecType().name(), OPCODE, getInputs().get(0).prepInputOperand(input1), getInputs().get(1).prepInputOperand(input2), prepOutputOperand(output), _cacheType.name(), String.valueOf(_outputEmptyBlocks));
    if (getExecType() == ExecType.SPARK)
        ret = InstructionUtils.concatOperands(ret, _aggtype.name());
    return ret;
}