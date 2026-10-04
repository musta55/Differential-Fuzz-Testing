public static UnionCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    if (!opcode.equalsIgnoreCase(Opcodes.UNION_DISTINCT.toString()))
        throw new DMLRuntimeException("Invalid opcode for UNION_DISTINCT: " + opcode);
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand in2 = new CPOperand(parts[2]);
    CPOperand out = new CPOperand(parts[parts.length - 2]);
    MultiThreadedOperator operator = new MultiThreadedOperator();
    return new UnionCPInstruction(operator, in1, in2, out, opcode, str);
}