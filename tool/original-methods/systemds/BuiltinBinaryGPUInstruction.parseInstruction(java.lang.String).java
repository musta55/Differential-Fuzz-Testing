public static BuiltinBinaryGPUInstruction parseInstruction(String str) {
    CPOperand in1 = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    CPOperand in2 = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    CPOperand out = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    InstructionUtils.checkNumFields(parts, 3);
    String opcode = parts[0];
    in1.split(parts[1]);
    in2.split(parts[2]);
    out.split(parts[3]);
    // check for valid data type of output
    if ((in1.getDataType() == DataType.MATRIX || in2.getDataType() == DataType.MATRIX) && out.getDataType() != DataType.MATRIX)
        throw new DMLRuntimeException("Element-wise matrix operations between variables " + in1.getName() + " and " + in2.getName() + " must produce a matrix, which " + out.getName() + " is not");
    // Determine appropriate Function Object based on opcode
    ValueFunction func = Builtin.getBuiltinFnObject(opcode);
    boolean isMatrixMatrix = in1.getDataType() == DataType.MATRIX && in2.getDataType() == DataType.MATRIX;
    boolean isMatrixScalar = (in1.getDataType() == DataType.MATRIX && in2.getDataType() == DataType.SCALAR) || (in1.getDataType() == DataType.SCALAR && in2.getDataType() == DataType.MATRIX);
    if (in1.getDataType() == DataType.SCALAR && in2.getDataType() == DataType.SCALAR)
        throw new DMLRuntimeException("GPU : Unsupported GPU builtin operations on 2 scalars");
    else if (isMatrixMatrix && opcode.equals("solve"))
        return new MatrixMatrixBuiltinGPUInstruction(new BinaryOperator(func), in1, in2, out, opcode, str, 2);
    else if (isMatrixScalar && (opcode.equals("min") || opcode.equals("max")))
        return new ScalarMatrixBuiltinGPUInstruction(new BinaryOperator(func), in1, in2, out, opcode, str, 2);
    else
        throw new DMLRuntimeException("GPU : Unsupported GPU builtin operations on a matrix and a scalar:" + opcode);
}