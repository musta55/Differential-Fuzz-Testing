public static MatrixMatrixAxpyGPUInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    InstructionUtils.checkNumFields(parts, 4);
    String opcode = parts[0];
    int multiplier = 1;
    if (opcode.equals("-*"))
        multiplier = -1;
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand constant = new CPOperand(parts[2]);
    if (constant.getDataType() != DataType.SCALAR)
        throw new DMLRuntimeException("Expected second operand to be a scalar");
    CPOperand in2 = new CPOperand(parts[3]);
    CPOperand out = new CPOperand(parts[4]);
    DataType dt1 = in1.getDataType();
    DataType dt2 = in2.getDataType();
    DataType dt3 = out.getDataType();
    Operator operator = (dt1 != dt2) ? InstructionUtils.parseScalarBinaryOperator(opcode, (dt1 == DataType.SCALAR)) : InstructionUtils.parseTernaryOperator(opcode);
    if (dt1 == DataType.MATRIX && dt2 == DataType.MATRIX && dt3 == DataType.MATRIX) {
        return new MatrixMatrixAxpyGPUInstruction(operator, in1, constant, multiplier, in2, out, opcode, str);
    } else if (dt3 == DataType.MATRIX && ((dt1 == DataType.SCALAR && dt2 == DataType.MATRIX) || (dt1 == DataType.MATRIX && dt2 == DataType.SCALAR))) {
        throw new DMLRuntimeException("Unsupported GPU PlusMult/MinusMult ArithmeticInstruction.");
        // return new ScalarMatrixArithmeticGPUInstruction(operator, in1, in2, out, opcode, str);
    } else
        throw new DMLRuntimeException("Unsupported GPU ArithmeticInstruction.");
}