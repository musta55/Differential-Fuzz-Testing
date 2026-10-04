public static AppendCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    InstructionUtils.checkNumFields(parts, 5, 4);
    String opcode = parts[0];
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand in2 = new CPOperand(parts[2]);
    CPOperand out = new CPOperand(parts[parts.length - 2]);
    boolean cbind = Boolean.parseBoolean(parts[parts.length - 1]);
    AppendType type = determineAppendType(in1, cbind);
    if (!isValidOpcode(opcode)) {
        throw new DMLRuntimeException("Unknown opcode while parsing a AppendCPInstruction: " + str);
    }
    Operator op = new ReorgOperator(OffsetColumnIndex.getOffsetColumnIndexFnObject(-1));
    return createAppendInstruction(op, in1, in2, out, type, opcode, str);
}
// ---- helper method(s) introduced by the refactoring ----
private static AppendType determineAppendType(CPOperand in1, boolean cbind) {
    if (in1.getDataType() != DataType.MATRIX && in1.getDataType() != DataType.FRAME) {
        return in1.getDataType() == DataType.LIST ? AppendType.LIST : AppendType.STRING;
    }
    return cbind ? AppendType.CBIND : AppendType.RBIND;
}

private static boolean isValidOpcode(String opcode) {
    return opcode.equalsIgnoreCase(Opcodes.APPEND.toString()) || opcode.equalsIgnoreCase(Opcodes.REMOVE.toString());
}

private static AppendCPInstruction createAppendInstruction(Operator op, CPOperand in1, CPOperand in2, CPOperand out, AppendType type, String opcode, String istr) {
    switch(type) {
        case STRING:
            return new ScalarAppendCPInstruction(op, in1, in2, out, type, opcode, istr);
        case LIST:
            return new ListAppendRemoveCPInstruction(op, in1, in2, out, type, opcode, istr);
        case CBIND:
            return new MatrixAppendCPInstruction(op, in1, in2, out, type, opcode, istr);
        case RBIND:
            return new FrameAppendCPInstruction(op, in1, in2, out, type, opcode, istr);
        default:
            throw new IllegalArgumentException("Unsupported append type: " + type);
    }
}

