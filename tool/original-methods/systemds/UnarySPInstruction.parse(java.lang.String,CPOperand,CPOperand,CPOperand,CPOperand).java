private static String parse(String instr, CPOperand in1, CPOperand in2, CPOperand in3, CPOperand out) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(instr);
    // first part is the opcode, last part is the output, middle parts are input operands
    String opcode = parts[0];
    out.split(parts[parts.length - 1]);
    switch(parts.length) {
        case 3:
            in1.split(parts[1]);
            in2 = null;
            in3 = null;
            break;
        case 4:
            in1.split(parts[1]);
            in2.split(parts[2]);
            in3 = null;
            break;
        case 5:
            in1.split(parts[1]);
            in2.split(parts[2]);
            in3.split(parts[3]);
            break;
        default:
            throw new DMLRuntimeException("Unexpected number of operands in the instruction: " + instr);
    }
    return opcode;
}