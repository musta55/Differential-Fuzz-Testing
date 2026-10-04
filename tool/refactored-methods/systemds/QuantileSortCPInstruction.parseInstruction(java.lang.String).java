public static QuantileSortCPInstruction parseInstruction(String str) {
    CPOperand in1 = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    CPOperand in2 = null;
    CPOperand out = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    //#threads
    int k = Integer.parseInt(parts[parts.length - 1]);
    if (parts.length == 4) {
        validateInstruction(str, opcode, 3);
        parseInstructionOperands(str, in1, null, out);
        return new QuantileSortCPInstruction(in1, out, opcode, str, k);
    } else if (parts.length == 5) {
        validateInstruction(str, opcode, 4);
        in2 = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
        parseInstructionOperands(str, in1, in2, out);
        return new QuantileSortCPInstruction(in1, in2, out, opcode, str, k);
    } else {
        throw new DMLRuntimeException("Invalid number of operands in instruction: " + str);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void parseInstructionOperands(String instr, CPOperand in1, CPOperand in2, CPOperand out) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(instr);
    out.split(parts[parts.length - 2]);
    switch(parts.length) {
        case 4:
            in1.split(parts[1]);
            in2 = null;
            break;
        case 5:
            in1.split(parts[1]);
            in2.split(parts[2]);
            break;
        default:
            throw new DMLRuntimeException("Unexpected number of operands in the instruction: " + instr);
    }
}

private static void validateInstruction(String str, String opcode, int expectedLength) {
    InstructionUtils.checkNumFields(str, expectedLength);
    if (!opcode.equalsIgnoreCase(SortKeys.OPCODE)) {
        throw new DMLRuntimeException("Unknown opcode while parsing a QuantileSortCPInstruction: " + str);
    }
}

