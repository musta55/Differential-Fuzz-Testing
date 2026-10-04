private DeCompressionCPInstruction(Operator op, CPOperand in, CPOperand out, String opcode, String istr) {
    super(CPType.Compression, op, in, null, null, out, opcode, istr);
}