protected BinaryFrameMatrixCPInstruction(Operator op, CPOperand in1, CPOperand in2, CPOperand out, String opcode, String istr) {
    super(CPInstruction.CPType.Binary, op, in1, in2, out, opcode, istr);
}