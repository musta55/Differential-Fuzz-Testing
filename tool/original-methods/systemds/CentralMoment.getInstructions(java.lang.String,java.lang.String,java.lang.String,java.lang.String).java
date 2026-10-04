/**
 * Function to generate CP centralMoment instruction for weighted operation.
 *
 * input1: data
 * input2: weights
 * input3: order
 */
@Override
public String getInstructions(String input1, String input2, String input3, String output) {
    StringBuilder sb = InstructionUtils.getStringBuilder();
    if (input3 == null) {
        InstructionUtils.concatOperands(sb, getExecType().toString(), Opcodes.CM.toString(), getInputs().get(0).prepInputOperand(input1), getInputs().get(1).prepScalarInputOperand(getExecType()), prepOutputOperand(output));
    } else {
        InstructionUtils.concatOperands(sb, getExecType().toString(), Opcodes.CM.toString(), getInputs().get(0).prepInputOperand(input1), getInputs().get(1).prepInputOperand(input2), getInputs().get(2).prepScalarInputOperand(getExecType()), prepOutputOperand(output));
    }
    if (getExecType() == ExecType.CP || getExecType() == ExecType.FED || getExecType() == ExecType.OOC) {
        sb.append(OPERAND_DELIMITOR);
        sb.append(_numThreads);
        if (getExecType() == ExecType.FED) {
            sb.append(OPERAND_DELIMITOR);
            sb.append(_fedOutput);
        }
    }
    return sb.toString();
}