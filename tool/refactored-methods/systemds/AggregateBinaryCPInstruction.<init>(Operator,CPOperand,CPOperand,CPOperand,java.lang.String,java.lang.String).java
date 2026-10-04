private AggregateBinaryCPInstruction(Operator op, CPOperand in1, CPOperand in2, CPOperand out, String opcode, String istr) {
    this(op, in1, in2, out, opcode, istr, false, false);
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

