@Override
public String getInstructions(String input1, String input2, String input3, String output) {
    StringBuilder sb = InstructionUtils.getStringBuilder();
    sb.append(getExecType());
    sb.append(OPERAND_DELIMITOR);
    sb.append(getOpCode());
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(0).prepInputOperand(input1));
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(1).prepInputOperand(input2));
    sb.append(OPERAND_DELIMITOR);
    sb.append(getInputs().get(2).prepInputOperand(input3));
    sb.append(OPERAND_DELIMITOR);
    sb.append(prepOutputOperand(output));
    appendNumThreads(sb);
    appendFedOutput(sb);
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private TernaryAggregate(Builder builder) {
    super(Lop.Type.TernaryAggregate, builder.dataType, builder.valueType);
    addInput(builder.input1);
    addInput(builder.input2);
    addInput(builder.input3);
    builder.input1.addOutput(this);
    builder.input2.addOutput(this);
    builder.input3.addOutput(this);
    _direction = builder.direction;
    _numThreads = builder.numThreads;
    lps.setProperties(inputs, builder.execType);
}

private void appendNumThreads(StringBuilder sb) {
    if (getExecType() == ExecType.CP || getExecType() == ExecType.FED) {
        sb.append(OPERAND_DELIMITOR);
        sb.append(_numThreads);
    }
}

private void appendFedOutput(StringBuilder sb) {
    if (getExecType() == ExecType.FED) {
        sb.append(OPERAND_DELIMITOR);
        sb.append(_fedOutput.name());
    }
}

