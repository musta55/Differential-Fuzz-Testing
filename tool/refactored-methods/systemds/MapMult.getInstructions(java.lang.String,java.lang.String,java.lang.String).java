@Override
public String getInstructions(String input1, String input2, String output) {
    StringBuilder sb = new StringBuilder();
    sb.append(InstructionUtils.concatOperands(getExecType().name(), OPCODE, getInputs().get(0).prepInputOperand(input1), getInputs().get(1).prepInputOperand(input2), prepOutputOperand(output), _cacheType.name(), String.valueOf(_outputEmptyBlocks)));
    if (getExecType() == ExecType.SPARK)
        sb.append(InstructionUtils.concatOperands(_aggtype.name()));
    return sb.toString();
}