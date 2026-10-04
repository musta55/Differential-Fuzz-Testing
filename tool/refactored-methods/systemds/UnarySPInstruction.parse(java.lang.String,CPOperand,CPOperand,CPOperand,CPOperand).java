private static String parse(String instr, CPOperand in1, CPOperand in2, CPOperand in3, CPOperand out) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(instr);
    // first part is the opcode, last part is the output, middle parts are input operands
    String opcode = parts[0];
    out.split(parts[parts.length - 1]);
    for (int i = 1; i < parts.length - 1; i++) {
        if (i == 1 && in1 != null) {
            in1.split(parts[i]);
        } else if (i == 2 && in2 != null) {
            in2.split(parts[i]);
        } else if (i == 3 && in3 != null) {
            in3.split(parts[i]);
        } else {
            throw new DMLRuntimeException("Unexpected number of operands in the instruction: " + instr);
        }
    }
    return opcode;
}