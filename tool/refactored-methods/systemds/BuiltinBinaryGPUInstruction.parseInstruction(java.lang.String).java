public static BuiltinBinaryGPUInstruction parseInstruction(String str) {
    InstructionParts parts = parseInstructionString(str);
    validateInstruction(parts);
    return createInstruction(parts);
}
// ---- helper method(s) introduced by the refactoring ----
private static InstructionParts parseInstructionString(String str) {
    CPOperand in1 = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    CPOperand in2 = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    CPOperand out = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    String[] instrParts = InstructionUtils.getInstructionPartsWithValueType(str);
    InstructionUtils.checkNumFields(instrParts, 4);
    String opcode = instrParts[0];
    in1.split(instrParts[1]);
    in2.split(instrParts[2]);
    out.split(instrParts[3]);
    return new InstructionParts(opcode, in1, in2, out, str);
}

private static void validateInstruction(InstructionParts parts) {
    if ((parts.in1.getDataType() == DataType.MATRIX || parts.in2.getDataType() == DataType.MATRIX) && parts.out.getDataType() != DataType.MATRIX)
        throw new DMLRuntimeException("Element-wise matrix operations between variables " + parts.in1.getName() + " and " + parts.in2.getName() + " must produce a matrix, which " + parts.out.getName() + " is not");
    if (parts.in1.getDataType() == DataType.SCALAR && parts.in2.getDataType() == DataType.SCALAR)
        throw new DMLRuntimeException("GPU : Unsupported GPU builtin operations on 2 scalars");
}

private static BuiltinBinaryGPUInstruction createInstruction(InstructionParts parts) {
    ValueFunction func = Builtin.getBuiltinFnObject(parts.opcode);
    boolean isMatrixMatrix = parts.in1.getDataType() == DataType.MATRIX && parts.in2.getDataType() == DataType.MATRIX;
    boolean isMatrixScalar = (parts.in1.getDataType() == DataType.MATRIX && parts.in2.getDataType() == DataType.SCALAR) || (parts.in1.getDataType() == DataType.SCALAR && parts.in2.getDataType() == DataType.MATRIX);
    if (isMatrixMatrix && parts.opcode.equals("solve"))
        return new MatrixMatrixBuiltinGPUInstruction(new BinaryOperator(func), parts.in1, parts.in2, parts.out, parts.opcode, parts.instructionString, 2);
    else if (isMatrixScalar && (parts.opcode.equals("min") || parts.opcode.equals("max")))
        return new ScalarMatrixBuiltinGPUInstruction(new BinaryOperator(func), parts.in1, parts.in2, parts.out, parts.opcode, parts.instructionString, 2);
    else
        throw new DMLRuntimeException("GPU : Unsupported GPU builtin operations on a matrix and a scalar:" + parts.opcode);
}

