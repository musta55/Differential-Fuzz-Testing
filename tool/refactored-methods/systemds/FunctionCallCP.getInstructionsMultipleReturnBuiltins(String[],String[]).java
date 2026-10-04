private String getInstructionsMultipleReturnBuiltins(String[] inputs, String[] outputs) {
    StringBuilder sb = new StringBuilder();
    sb.append(getExecType());
    sb.append(Lop.OPERAND_DELIMITOR);
    sb.append(_fname.toLowerCase());
    for (String input : inputs) {
        sb.append(Lop.OPERAND_DELIMITOR);
        sb.append(getInputs().get(0).prepInputOperand(input));
    }
    for (String output : _outputNames) {
        sb.append(Lop.OPERAND_DELIMITOR);
        sb.append(output);
    }
    if (getExecType().equals(ExecType.CP) && !_fname.equalsIgnoreCase(Opcodes.REMOVE.toString())) {
        sb.append(Lop.OPERAND_DELIMITOR);
        sb.append(_numThreads);
    }
    return sb.toString();
}