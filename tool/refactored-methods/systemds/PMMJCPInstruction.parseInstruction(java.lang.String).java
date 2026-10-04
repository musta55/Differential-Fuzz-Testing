public static PMMJCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    validateInstructionParts(parts);
    String opcode = parts[0];
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand in2 = new CPOperand(parts[2]);
    CPOperand in3 = new CPOperand(parts[3]);
    CPOperand out = new CPOperand(parts[4]);
    int k = Integer.parseInt(parts[5]);
    return new Builder().operator(new Operator(true)).in1(in1).in2(in2).in3(in3).out(out).k(k).opcode(opcode).istr(str).build();
}
// ---- helper method(s) introduced by the refactoring ----
private PMMJCPInstruction(Builder builder) {
    super(CPType.AggregateBinary, builder.operator, builder.in1, builder.in2, builder.in3, builder.out, builder.opcode, builder.istr);
    _numThreads = builder.k;
}

private static void validateInstructionParts(String[] parts) {
    InstructionUtils.checkNumFields(parts, 5);
}

