protected AppendSPInstruction(SPType type, Operator op, CPOperand input1, CPOperand input2, CPOperand output, boolean cbind, String opcode, String instructionString) {
    super(type, op, input1, input2, output, opcode, instructionString);
    _cbind = cbind;
}