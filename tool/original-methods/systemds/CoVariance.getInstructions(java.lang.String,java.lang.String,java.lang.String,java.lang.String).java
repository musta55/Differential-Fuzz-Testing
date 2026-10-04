/**
 * Function two generate CP instruction to compute weighted covariance.
 * input1 -&gt; input column 1
 * input2 -&gt; input column 2
 * input3 -&gt; weights
 */
@Override
public String getInstructions(String input1, String input2, String input3, String output) {
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