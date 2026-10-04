public static MatrixMatrixAxpyGPUInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    validateInstructionParts(parts);
    String opcode = parts[0];
    int multiplier = determineMultiplier(opcode);
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand constant = new CPOperand(parts[2]);
    validateConstant(constant);
    CPOperand in2 = new CPOperand(parts[3]);
    CPOperand out = new CPOperand(parts[4]);
    DataType dt1 = in1.getDataType();
    DataType dt2 = in2.getDataType();
    DataType dt3 = out.getDataType();
    Operator operator = (dt1 != dt2) ? InstructionUtils.parseScalarBinaryOperator(opcode, (dt1 == DataType.SCALAR)) : InstructionUtils.parseTernaryOperator(opcode);
    if (dt1 == DataType.MATRIX && dt2 == DataType.MATRIX && dt3 == DataType.MATRIX) {
        return new Builder().operator(operator).in1(in1).constant(constant).multiplier(multiplier).in2(in2).out(out).opcode(opcode).istr(str).build();
    } else if (dt3 == DataType.MATRIX && ((dt1 == DataType.SCALAR && dt2 == DataType.MATRIX) || (dt1 == DataType.MATRIX && dt2 == DataType.SCALAR))) {
        throw new DMLRuntimeException("Unsupported GPU PlusMult/MinusMult ArithmeticInstruction.");
        // return new ScalarMatrixArithmeticGPUInstruction(operator, in1, in2, out, opcode, str);
    } else {
        throw new DMLRuntimeException("Unsupported GPU ArithmeticInstruction.");
    }
}
// ---- helper method(s) introduced by the refactoring ----
private MatrixMatrixAxpyGPUInstruction(Builder builder) {
    super(builder.operator, builder.in1, builder.in2, builder.out, builder.opcode, builder.istr);
    this.constant = builder.constant;
    this.multiplier = builder.multiplier;
}

private static void validateInstructionParts(String[] parts) {
    InstructionUtils.checkNumFields(parts, 5);
}

private static int determineMultiplier(String opcode) {
    return "-*".equals(opcode) ? -1 : 1;
}

private static void validateConstant(CPOperand constant) {
    if (constant.getDataType() != DataType.SCALAR) {
        throw new DMLRuntimeException("Expected second operand to be a scalar");
    }
}

