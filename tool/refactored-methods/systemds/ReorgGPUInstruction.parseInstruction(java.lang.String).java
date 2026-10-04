public static ReorgGPUInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    validateInstructionParts(parts);
    String opcode = parts[0];
    CPOperand in = new CPOperand(parts[1]);
    CPOperand out = new CPOperand(parts[2]);
    if (!opcode.equalsIgnoreCase("r'")) {
        throw new DMLRuntimeException("Unknown opcode while parsing a ReorgInstruction: " + str);
    } else {
        return new ReorgGPUInstruction(new Builder().operator(new ReorgOperator(SwapIndex.getSwapIndexFnObject())).input(in).output(out).opcode(opcode).instructionString(str));
    }
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * for opcodes r'
 *
 * @param op
 *            operator
 * @param in
 *            input operand
 * @param out
 *            output operand
 * @param opcode
 *            the opcode
 * @param istr
 *            instruction string
 */
private ReorgGPUInstruction(Builder builder) {
    super(builder.operator, builder.input, null, builder.output, builder.opcode, builder.instructionString);
    _gputype = GPUINSTRUCTION_TYPE.Reorg;
}

private static void validateInstructionParts(String[] parts) {
    InstructionUtils.checkNumFields(parts, 3);
}

