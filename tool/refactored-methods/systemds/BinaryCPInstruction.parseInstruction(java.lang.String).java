public static BinaryCPInstruction parseInstruction(String str) {
    CPOperand in1 = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    CPOperand in2 = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    CPOperand out = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    final String[] parts = parseBinaryInstruction(str, in1, in2, out);
    final String opcode = parts[0];
    if (!isFrameOperand(in1) && !isFrameOperand(in2))
        checkOutputDataType(in1, in2, out);
    MultiThreadedOperator operator = InstructionUtils.parseBinaryOrBuiltinOperator(opcode, in1, in2);
    if (parts.length == 5 && operator != null)
        operator.setNumThreads(Integer.parseInt(parts[4]));
    return createBinaryInstruction(operator, in1, in2, out, opcode, str);
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean isFrameOperand(CPOperand operand) {
    return operand.getDataType() == DataType.FRAME;
}

private static BinaryCPInstruction createBinaryInstruction(MultiThreadedOperator operator, CPOperand in1, CPOperand in2, CPOperand out, String opcode, String str) {
    if (isScalarOperand(in1) && isScalarOperand(in2))
        return new BinaryScalarScalarCPInstruction(operator, in1, in2, out, opcode, str);
    else if (isMatrixOperand(in1) && isMatrixOperand(in2))
        return new BinaryMatrixMatrixCPInstruction(operator, in1, in2, out, opcode, str);
    else if (isTensorOperand(in1) && isTensorOperand(in2))
        return new BinaryTensorTensorCPInstruction(operator, in1, in2, out, opcode, str);
    else if (isFrameOperand(in1) && isFrameOperand(in2))
        return new BinaryFrameFrameCPInstruction(operator, in1, in2, out, opcode, str);
    else if (isFrameOperand(in1) && isScalarOperand(in2))
        return new BinaryFrameScalarCPInstruction(operator, in1, in2, out, opcode, str);
    else if (isFrameOperand(in1) && isMatrixOperand(in2))
        return new BinaryFrameMatrixCPInstruction(operator, in1, in2, out, opcode, str);
    else
        return new BinaryMatrixScalarCPInstruction(operator, in1, in2, out, opcode, str);
}

private static boolean isScalarOperand(CPOperand operand) {
    return operand.getDataType() == DataType.SCALAR;
}

private static boolean isMatrixOperand(CPOperand operand) {
    return operand.getDataType() == DataType.MATRIX;
}

private static boolean isTensorOperand(CPOperand operand) {
    return operand.getDataType() == DataType.TENSOR;
}

