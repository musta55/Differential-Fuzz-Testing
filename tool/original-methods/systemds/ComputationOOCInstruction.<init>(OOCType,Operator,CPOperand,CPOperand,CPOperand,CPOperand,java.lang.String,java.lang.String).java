protected ComputationOOCInstruction(OOCType type, Operator op, CPOperand in1, CPOperand in2, CPOperand in3, CPOperand out, String opcode, String istr) {
    super(type, op, opcode, istr);
    input1 = in1;
    input2 = in2;
    input3 = in3;
    output = out;
}