public static ArithmeticBinaryGPUInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    InstructionUtils.checkNumFields(parts, 3);
    String opcode = parts[0];
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand in2 = new CPOperand(parts[2]);
    CPOperand out = new CPOperand(parts[3]);
    DataType dt1 = in1.getDataType();
    DataType dt2 = in2.getDataType();
    DataType dt3 = out.getDataType();
    Operator operator = (dt1 != dt2) ? InstructionUtils.parseScalarBinaryOperator(opcode, (dt1 == DataType.SCALAR)) : InstructionUtils.parseBinaryOperator(opcode);
    if (dt1 == DataType.MATRIX && dt2 == DataType.MATRIX && dt3 == DataType.MATRIX) {
        return new MatrixMatrixArithmeticGPUInstruction(operator, in1, in2, out, opcode, str);
    } else if (dt3 == DataType.MATRIX && ((dt1 == DataType.SCALAR && dt2 == DataType.MATRIX) || (dt1 == DataType.MATRIX && dt2 == DataType.SCALAR))) {
        return new ScalarMatrixArithmeticGPUInstruction(operator, in1, in2, out, opcode, str);
    } else
        throw new DMLRuntimeException("Unsupported GPU ArithmeticInstruction.");
}