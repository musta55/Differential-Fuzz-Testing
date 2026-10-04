public static EvictCPInstruction parseInstruction(String str) {
    InstructionUtils.checkNumFields(str, 3);
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    CPOperand in = new CPOperand(parts[1]);
    CPOperand out = new CPOperand(parts[2]);
    return new Builder().opcode(opcode).input(in).output(out).instructionString(str).build();
}
// ---- helper method(s) introduced by the refactoring ----
private EvictCPInstruction(Builder builder) {
    super(CPType.EvictLineageCache, builder.operator, builder.input, builder.output, builder.opcode, builder.instructionString);
}

