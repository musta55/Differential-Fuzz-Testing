public static BroadcastCPInstruction parseInstruction(String str) {
    InstructionUtils.checkNumFields(str, 2, 3);
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    CPOperand in = new CPOperand(parts[1]);
    CPOperand out = new CPOperand(parts[2]);
    return new BroadcastCPInstruction(new Builder(null, in, out, opcode, str));
}
// ---- helper method(s) introduced by the refactoring ----
private BroadcastCPInstruction(Builder builder) {
    super(CPType.Broadcast, builder.operator, builder.input, builder.output, builder.opcode, builder.instructionString);
}

