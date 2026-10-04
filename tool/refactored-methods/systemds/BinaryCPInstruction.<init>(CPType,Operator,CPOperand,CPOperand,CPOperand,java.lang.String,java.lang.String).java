protected BinaryCPInstruction(CPType type, Operator op, CPOperand in1, CPOperand in2, CPOperand out, String opcode, String istr) {
    this(type, op, in1, in2, null, out, opcode, istr);
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

