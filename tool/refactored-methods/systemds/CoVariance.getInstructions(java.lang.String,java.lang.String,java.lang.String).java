/**
 * Function two generate CP instruction to compute unweighted covariance.
 * input1 -&gt; input column 1
 * input2 -&gt; input column 2
 */
@Override
public String getInstructions(String input1, String input2, String output) {
    return buildInstructionString(input1, input2, null, output);
}
// ---- helper method(s) introduced by the refactoring ----
private void initInputs(Lop input1, Lop input2, Lop input3) {
    if (input2 == null)
        throw new LopsException(this.printErrorLocation() + "Invalid inputs to covariance lop.");
    addInput(input1);
    input1.addOutput(this);
    addInput(input2);
    input2.addOutput(this);
    if (input3 != null) {
        addInput(input3);
        input3.addOutput(this);
    }
}

private void setExecutionProperties(ExecType et) {
    lps.setProperties(inputs, et);
}

private String buildInstructionString(String input1, String input2, String input3, String output) {
    StringBuilder sb = InstructionUtils.getStringBuilder();
    sb.append(getExecType());
    sb.append(Lop.OPERAND_DELIMITOR);
    sb.append(Opcodes.COV.toString());
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(0).prepInputOperand(input1));
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(1).prepInputOperand(input2));
    sb.append(OPERAND_DELIMITOR);
    if (input3 != null) {
        sb.append(getInputs().get(2).prepInputOperand(input3));
        sb.append(OPERAND_DELIMITOR);
    }
    sb.append(prepOutputOperand(output));
    if (getExecType() == ExecType.CP || getExecType() == ExecType.FED) {
        sb.append(OPERAND_DELIMITOR);
        sb.append(_numThreads);
        if (getExecType() == ExecType.FED) {
            sb.append(OPERAND_DELIMITOR);
            sb.append(_fedOutput);
        }
    }
    return sb.toString();
}

