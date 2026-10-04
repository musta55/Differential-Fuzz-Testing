public static AppendRSPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    validateInstructionParts(parts);
    String opcode = parts[0];
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand in2 = new CPOperand(parts[2]);
    CPOperand out = new CPOperand(parts[3]);
    boolean cbind = Boolean.parseBoolean(parts[4]);
    if (!opcode.equalsIgnoreCase(Opcodes.RAPPEND.toString())) {
        throw new DMLRuntimeException("Unknown opcode while parsing a MatrixAppendRSPInstruction: " + str);
    }
    if (in1.getDataType().isMatrix()) {
        return new MatrixAppendRSPInstruction(new ReorgOperator(OffsetColumnIndex.getOffsetColumnIndexFnObject(-1)), in1, in2, out, cbind, opcode, str);
    } else {
        // frame
        return new FrameAppendRSPInstruction(new ReorgOperator(OffsetColumnIndex.getOffsetColumnIndexFnObject(-1)), in1, in2, out, cbind, opcode, str);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void validateInstructionParts(String[] parts) {
    InstructionUtils.checkNumFields(parts, 5);
}

