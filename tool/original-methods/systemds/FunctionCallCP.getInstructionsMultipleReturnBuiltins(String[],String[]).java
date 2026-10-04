private String getInstructionsMultipleReturnBuiltins(String[] inputs, String[] outputs) {
    StringBuilder sb = new StringBuilder();
    sb.append(getExecType());
    sb.append(Lop.OPERAND_DELIMITOR);
    sb.append(_fname.toLowerCase());
    for (int i = 0; i < inputs.length; i++) {
        sb.append(Lop.OPERAND_DELIMITOR);
        sb.append(getInputs().get(i).prepInputOperand(inputs[i]));
    }
    for (int i = 0; i < _outputNames.length; i++) {
        sb.append(Lop.OPERAND_DELIMITOR);
        sb.append(_outputNames[i]);
    }
    if (getExecType().equals(ExecType.CP)) {
        if (!(_fname.toLowerCase().equals(Opcodes.REMOVE.toString()))) {
            sb.append(Lop.OPERAND_DELIMITOR);
            sb.append(_numThreads);
        }
    }
    return sb.toString();
}