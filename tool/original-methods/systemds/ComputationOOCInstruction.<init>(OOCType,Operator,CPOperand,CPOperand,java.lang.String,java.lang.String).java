protected ComputationOOCInstruction(OOCType type, Operator op, CPOperand in1, CPOperand out, String opcode, String istr) {
    super(type, op, opcode, istr);
    input1 = in1;
    input2 = null;
    input3 = null;
    output = out;
}