public static BuiltinUnaryGPUInstruction parseInstruction(String str) {
    CPOperand in = new CPOperand();
    CPOperand out = new CPOperand();
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = validateAndExtractOpcode(parts, str);
    in.split(parts[1]);
    out.split(parts[2]);
    validateInputDataType(in, str);
    return createInstructionInstance(null, in, out, opcode, str);
}
// ---- helper method(s) introduced by the refactoring ----
private static String validateAndExtractOpcode(String[] parts, String str) {
    if (parts.length == 4) {
        throw new DMLRuntimeException("The instruction is not supported on GPU: " + str);
    }
    InstructionUtils.checkNumFields(str, 2);
    return parts[0];
}

private static void validateInputDataType(CPOperand in, String str) {
    if (in.getDataType() == DataType.SCALAR) {
        throw new DMLRuntimeException("The instruction is not supported on GPU: " + str);
    }
}

private static BuiltinUnaryGPUInstruction createInstructionInstance(Operator op, CPOperand in, CPOperand out, String opcode, String str) {
    if (in.getDataType() == DataType.MATRIX) {
        return new MatrixBuiltinGPUInstruction(op, in, out, opcode, str);
    }
    return null;
}

