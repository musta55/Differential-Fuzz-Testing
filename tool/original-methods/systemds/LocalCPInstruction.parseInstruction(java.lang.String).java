public static LocalCPInstruction parseInstruction(String str) {
    InstructionUtils.checkNumFields(str, 2);
    final String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    final CPOperand in = new CPOperand(parts[1]);
    final CPOperand out = new CPOperand(parts[2]);
    return new LocalCPInstruction(in, out, str);
}