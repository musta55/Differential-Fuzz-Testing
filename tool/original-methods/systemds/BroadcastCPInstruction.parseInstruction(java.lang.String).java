public static BroadcastCPInstruction parseInstruction(String str) {
    InstructionUtils.checkNumFields(str, 2, 3);
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    CPOperand in = new CPOperand(parts[1]);
    CPOperand out = new CPOperand(parts[2]);
    return new BroadcastCPInstruction(null, in, out, opcode, str);
}