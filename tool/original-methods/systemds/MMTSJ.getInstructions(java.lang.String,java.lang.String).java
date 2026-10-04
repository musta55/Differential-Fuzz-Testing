@Override
public String getInstructions(String input_index1, String output_index) {
    StringBuilder sb = InstructionUtils.getStringBuilder();
    sb.append(getExecType());
    sb.append(OPERAND_DELIMITOR);
    sb.append(_multiPass ? Opcodes.TSMM2.toString() : Opcodes.TSMM.toString());
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(0).prepInputOperand(input_index1));
    sb.append(OPERAND_DELIMITOR);
    sb.append(prepOutputOperand(output_index));
    sb.append(OPERAND_DELIMITOR);
    sb.append(_type);
    //append degree of parallelism for matrix multiplications
    if (getExecType() == ExecType.CP || getExecType() == ExecType.FED) {
        sb.append(OPERAND_DELIMITOR);
        sb.append(_numThreads);
        if (getExecType() == ExecType.FED) {
            sb.append(OPERAND_DELIMITOR);
            sb.append(_fedOutput.name());
        }
    }
    return sb.toString();
}