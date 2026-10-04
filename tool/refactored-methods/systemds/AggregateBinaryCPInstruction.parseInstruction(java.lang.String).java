public static AggregateBinaryCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    validateOpcode(opcode);
    int numFields = InstructionUtils.checkNumFields(parts, 4, 6);
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand in2 = new CPOperand(parts[2]);
    CPOperand out = new CPOperand(parts[3]);
    int k = Integer.parseInt(parts[4]);
    AggregateBinaryOperator op = InstructionUtils.getMatMultOperator(k);
    boolean[] transposes = parseTransposes(parts, numFields);
    return new AggregateBinaryCPInstruction(op, in1, in2, out, opcode, str, transposes[0], transposes[1]);
}
// ---- helper method(s) introduced by the refactoring ----
private static void validateOpcode(String opcode) {
    if (!opcode.equalsIgnoreCase(Opcodes.MMULT.toString())) {
        throw new DMLRuntimeException("AggregateBinaryInstruction.parseInstruction():: Unknown opcode " + opcode);
    }
}

private static boolean[] parseTransposes(String[] parts, int numFields) {
    boolean lt = false;
    boolean rt = false;
    if (numFields == 6) {
        lt = Boolean.parseBoolean(parts[5]);
        rt = Boolean.parseBoolean(parts[6]);
    }
    return new boolean[] { lt, rt };
}

