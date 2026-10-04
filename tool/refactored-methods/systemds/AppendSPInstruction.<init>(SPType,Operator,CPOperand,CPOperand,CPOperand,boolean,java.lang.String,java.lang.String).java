protected AppendSPInstruction(SPType type, Operator op, CPOperand input1, CPOperand input2, CPOperand output, boolean cbind, String opcode, String instructionString) {
    this(type, op, input1, input2, output, cbind, opcode, instructionString, null);
}
// ---- helper method(s) introduced by the refactoring ----
protected AppendSPInstruction(SPType type, Operator op, CPOperand input1, CPOperand input2, CPOperand output, boolean cbind, String opcode, String instructionString, Object unused) {
    super(type, op, input1, input2, output, opcode, instructionString);
    _cbind = cbind;
}

