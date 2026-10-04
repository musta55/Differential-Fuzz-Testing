public static AppendCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    InstructionUtils.checkNumFields(parts, 5, 4);
    String opcode = parts[0];
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand in2 = new CPOperand(parts[2]);
    CPOperand out = new CPOperand(parts[parts.length - 2]);
    boolean cbind = Boolean.parseBoolean(parts[parts.length - 1]);
    AppendType type = (in1.getDataType() != DataType.MATRIX && in1.getDataType() != DataType.FRAME) ? in1.getDataType() == DataType.LIST ? AppendType.LIST : AppendType.STRING : cbind ? AppendType.CBIND : AppendType.RBIND;
    if (!opcode.equalsIgnoreCase(Opcodes.APPEND.toString()) && !opcode.equalsIgnoreCase(Opcodes.REMOVE.toString()))
        throw new DMLRuntimeException("Unknown opcode while parsing a AppendCPInstruction: " + str);
    Operator op = new ReorgOperator(OffsetColumnIndex.getOffsetColumnIndexFnObject(-1));
    if (type == AppendType.STRING)
        return new ScalarAppendCPInstruction(op, in1, in2, out, type, opcode, str);
    else if (type == AppendType.LIST)
        return new ListAppendRemoveCPInstruction(op, in1, in2, out, type, opcode, str);
    else if (in1.getDataType() == DataType.MATRIX)
        return new MatrixAppendCPInstruction(op, in1, in2, out, type, opcode, str);
    else
        //DataType.FRAME
        return new FrameAppendCPInstruction(op, in1, in2, out, type, opcode, str);
}