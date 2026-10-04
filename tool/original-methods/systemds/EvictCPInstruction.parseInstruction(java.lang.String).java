public static EvictCPInstruction parseInstruction(String str) {
    InstructionUtils.checkNumFields(str, 3);
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    CPOperand in = new CPOperand(parts[1]);
    CPOperand out = new CPOperand(parts[2]);
    return new EvictCPInstruction(null, in, out, opcode, str);
}