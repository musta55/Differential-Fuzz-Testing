protected AppendCPInstruction(Operator op, CPOperand in1, CPOperand in2, CPOperand out, AppendType type, String opcode, String istr) {
    super(CPType.Append, op, in1, in2, out, opcode, istr);
    this._type = type;
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

