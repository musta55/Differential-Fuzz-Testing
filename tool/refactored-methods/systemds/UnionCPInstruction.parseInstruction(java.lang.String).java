public static UnionCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    if (!opcode.equalsIgnoreCase(Opcodes.UNION_DISTINCT.toString()))
        throw new DMLRuntimeException("Invalid opcode for UNION_DISTINCT: " + opcode);
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand in2 = new CPOperand(parts[2]);
    CPOperand out = new CPOperand(parts[parts.length - 2]);
    MultiThreadedOperator operator = new MultiThreadedOperator();
    return new Builder().operator(operator).in1(in1).in2(in2).out(out).opcode(opcode).istr(str).build();
}
// ---- helper method(s) introduced by the refactoring ----
private UnionCPInstruction(Builder builder) {
    super(CPType.Union, builder.operator, builder.in1, builder.in2, builder.out, builder.opcode, builder.istr);
}

