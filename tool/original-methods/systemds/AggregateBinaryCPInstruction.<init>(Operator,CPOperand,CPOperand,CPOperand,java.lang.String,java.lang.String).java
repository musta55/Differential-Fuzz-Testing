private AggregateBinaryCPInstruction(Operator op, CPOperand in1, CPOperand in2, CPOperand out, String opcode, String istr) {
    super(CPType.AggregateBinary, op, in1, in2, out, opcode, istr);
    transposeLeft = false;
    transposeRight = false;
}