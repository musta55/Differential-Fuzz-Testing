protected ComputationOOCInstruction(OOCType type, Operator op, CPOperand in1, CPOperand in2, CPOperand out, String opcode, String istr) {
    this(type, op, new CPOperand[] { in1, in2 }, out, opcode, istr);
}
// ---- helper method(s) introduced by the refactoring ----
private ComputationOOCInstruction(OOCType type, Operator op, CPOperand[] inputs, CPOperand out, String opcode, String istr) {
    super(type, op, opcode, istr);
    if (inputs.length > 0)
        input1 = inputs[0];
    if (inputs.length > 1)
        input2 = inputs[1];
    if (inputs.length > 2)
        input3 = inputs[2];
    if (inputs.length > 3)
        input4 = inputs[3];
    output = out;
}

