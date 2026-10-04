public static MMTSJGPUInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    validateInstructionParts(parts);
    String opcode = parts[0];
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand out = new CPOperand(parts[2]);
    MMTSJType type = MMTSJType.valueOf(parts[3]);
    return new MMTSJGPUInstruction(new Builder().op(new Operator(true)).in1(in1).type(type).out(out).opcode(opcode).istr(str));
}
// ---- helper method(s) introduced by the refactoring ----
/**
 *  MMTSJGPUInstruction constructor.
 *
 *  @param op
 * 			operator
 *  @param in1
 * 			input
 *  @param type
 * 			left/right, left-&gt; A' %*% A, right-&gt; A %*% A'
 *  @param out
 * 			output
 *  @param opcode
 * 			the opcode
 *  @param istr
 * 			?
 */
private MMTSJGPUInstruction(Builder builder) {
    super(builder.op, builder.in1, null, builder.out, builder.opcode, builder.istr);
    _gputype = GPUINSTRUCTION_TYPE.MMTSJ;
    _type = builder.type;
}

private static void validateInstructionParts(String[] parts) {
    InstructionUtils.checkNumFields(parts, 4);
    if (!parts[0].equalsIgnoreCase("tsmm")) {
        throw new DMLRuntimeException("Unknown opcode while parsing an MMTSJGPUInstruction: " + String.join(" ", parts));
    }
}

