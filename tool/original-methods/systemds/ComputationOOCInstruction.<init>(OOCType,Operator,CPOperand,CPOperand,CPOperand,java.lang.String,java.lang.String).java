protected ComputationOOCInstruction(OOCType type, Operator op, CPOperand in1, CPOperand in2, CPOperand out, String opcode, String istr) {
    super(type, op, opcode, istr);
    input1 = in1;
    input2 = in2;
    input3 = null;
    output = out;
}