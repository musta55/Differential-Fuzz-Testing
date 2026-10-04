public static IndexingCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    if (opcode.equalsIgnoreCase(Opcodes.RIGHT_INDEX.toString())) {
        validateOperandCount(parts, 7);
        CPOperand in = new CPOperand(parts[1]);
        CPOperand rl = new CPOperand(parts[2]);
        CPOperand ru = new CPOperand(parts[3]);
        CPOperand cl = new CPOperand(parts[4]);
        CPOperand cu = new CPOperand(parts[5]);
        CPOperand out = new CPOperand(parts[6]);
        return createIndexingInstruction(in, null, rl, ru, cl, cu, out, opcode, str);
    } else if (opcode.equalsIgnoreCase(Opcodes.LEFT_INDEX.toString())) {
        validateOperandCount(parts, 8);
        CPOperand lhsInput = new CPOperand(parts[1]);
        CPOperand rhsInput = new CPOperand(parts[2]);
        CPOperand rl = new CPOperand(parts[3]);
        CPOperand ru = new CPOperand(parts[4]);
        CPOperand cl = new CPOperand(parts[5]);
        CPOperand cu = new CPOperand(parts[6]);
        CPOperand out = new CPOperand(parts[7]);
        return createIndexingInstruction(lhsInput, rhsInput, rl, ru, cl, cu, out, opcode, str);
    } else {
        throw new DMLRuntimeException("Unknown opcode while parsing a MatrixIndexingCPInstruction: " + str);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void validateOperandCount(String[] parts, int expectedCount) {
    if (parts.length != expectedCount) {
        throw new DMLRuntimeException("Invalid number of operands in instruction: " + String.join(" ", parts));
    }
}

private static IndexingCPInstruction createIndexingInstruction(CPOperand input, CPOperand rhsInput, CPOperand rl, CPOperand ru, CPOperand cl, CPOperand cu, CPOperand out, String opcode, String istr) {
    switch(input.getDataType()) {
        case MATRIX:
            return new MatrixIndexingCPInstruction(input, rhsInput, rl, ru, cl, cu, out, opcode, istr);
        case FRAME:
            return new FrameIndexingCPInstruction(input, rhsInput, rl, ru, cl, cu, out, opcode, istr);
        case LIST:
            return new ListIndexingCPInstruction(input, rhsInput, rl, ru, cl, cu, out, opcode, istr);
        default:
            throw new DMLRuntimeException("Can index only on matrices, frames, and lists.");
    }
}

