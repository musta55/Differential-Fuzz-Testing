public static MMTSJCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    validateInstructionParts(parts);
    String opcode = parts[0];
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand out = new CPOperand(parts[2]);
    MMTSJType titype = MMTSJType.valueOf(parts[3]);
    int k = Integer.parseInt(parts[4]);
    return new Builder().operator(new Operator(true)).input1(in1).output(out).type(titype).numThreads(k).opcode(opcode).instructionString(str).build();
}
// ---- helper method(s) introduced by the refactoring ----
private MMTSJCPInstruction(Builder builder) {
    super(CPType.MMTSJ, builder.operator, builder.input1, builder.output, builder.opcode, builder.instructionString);
    _type = builder.type;
    _numThreads = builder.numThreads;
}

private static void validateInstructionParts(String[] parts) {
    InstructionUtils.checkNumFields(parts, 5);
    String opcode = parts[0];
    if (!opcode.equalsIgnoreCase(Opcodes.TSMM.toString())) {
        throw new DMLRuntimeException("Unknown opcode while parsing an MMTSJCPInstruction: " + String.join(" ", parts));
    }
}

