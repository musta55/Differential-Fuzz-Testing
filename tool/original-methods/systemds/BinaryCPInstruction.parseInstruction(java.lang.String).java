public static BinaryCPInstruction parseInstruction(String str) {
    CPOperand in1 = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    CPOperand in2 = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    CPOperand out = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    final String[] parts = parseBinaryInstruction(str, in1, in2, out);
    final String opcode = parts[0];
    if (!(in1.getDataType() == DataType.FRAME || in2.getDataType() == DataType.FRAME))
        checkOutputDataType(in1, in2, out);
    MultiThreadedOperator operator = InstructionUtils.parseBinaryOrBuiltinOperator(opcode, in1, in2);
    if (parts.length == 5 && operator != null)
        operator.setNumThreads(Integer.parseInt(parts[4]));
    if (in1.getDataType() == DataType.SCALAR && in2.getDataType() == DataType.SCALAR)
        return new BinaryScalarScalarCPInstruction(operator, in1, in2, out, opcode, str);
    else if (in1.getDataType() == DataType.MATRIX && in2.getDataType() == DataType.MATRIX)
        return new BinaryMatrixMatrixCPInstruction(operator, in1, in2, out, opcode, str);
    else if (in1.getDataType() == DataType.TENSOR && in2.getDataType() == DataType.TENSOR)
        return new BinaryTensorTensorCPInstruction(operator, in1, in2, out, opcode, str);
    else if (in1.getDataType() == DataType.FRAME && in2.getDataType() == DataType.FRAME)
        return new BinaryFrameFrameCPInstruction(operator, in1, in2, out, opcode, str);
    else if (in1.getDataType() == DataType.FRAME && in2.getDataType() == DataType.SCALAR)
        return new BinaryFrameScalarCPInstruction(operator, in1, in2, out, opcode, str);
    else if (in1.getDataType() == DataType.FRAME && in2.getDataType() == DataType.MATRIX)
        return new BinaryFrameMatrixCPInstruction(operator, in1, in2, out, opcode, str);
    else
        return new BinaryMatrixScalarCPInstruction(operator, in1, in2, out, opcode, str);
}