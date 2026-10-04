public static RelationalBinaryGPUInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    InstructionUtils.checkNumFields(parts, 4);
    String opcode = parseOpcode(parts);
    CPOperand in1 = parseOperand(parts, 1);
    CPOperand in2 = parseOperand(parts, 2);
    CPOperand out = parseOperand(parts, 3);
    DataType dt1 = in1.getDataType();
    DataType dt2 = in2.getDataType();
    DataType dt3 = out.getDataType();
    Operator operator = parseOperator(opcode, dt1, dt2);
    return createInstruction(operator, in1, in2, out, opcode, str, dt1, dt2, dt3);
}
// ---- helper method(s) introduced by the refactoring ----
private static String parseOpcode(String[] parts) {
    return parts[0];
}

private static CPOperand parseOperand(String[] parts, int index) {
    return new CPOperand(parts[index]);
}

private static Operator parseOperator(String opcode, DataType dt1, DataType dt2) {
    return (dt1 != dt2) ? InstructionUtils.parseScalarBinaryOperator(opcode, (dt1 == DataType.SCALAR)) : InstructionUtils.parseBinaryOperator(opcode);
}

private static RelationalBinaryGPUInstruction createInstruction(Operator operator, CPOperand in1, CPOperand in2, CPOperand out, String opcode, String str, DataType dt1, DataType dt2, DataType dt3) {
    if (dt1 == DataType.MATRIX && dt2 == DataType.MATRIX && dt3 == DataType.MATRIX) {
        return new MatrixMatrixRelationalBinaryGPUInstruction(operator, in1, in2, out, opcode, str);
    } else if (dt3 == DataType.MATRIX && ((dt1 == DataType.SCALAR && dt2 == DataType.MATRIX) || (dt1 == DataType.MATRIX && dt2 == DataType.SCALAR))) {
        return new ScalarMatrixRelationalBinaryGPUInstruction(operator, in1, in2, out, opcode, str);
    } else {
        throw new DMLRuntimeException("Unsupported GPU RelationalBinaryGPUInstruction.");
    }
}

