protected AppendCPInstruction(Operator op, CPOperand in1, CPOperand in2, CPOperand out, AppendType type, String opcode, String istr) {
    super(CPType.Append, op, in1, in2, out, opcode, istr);
    _type = type;
}