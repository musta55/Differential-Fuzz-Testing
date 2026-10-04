public static LocalCPInstruction parseInstruction(String str) {
    InstructionUtils.checkNumFields(str, 2);
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    CPOperand in = new CPOperand(parts[1]);
    CPOperand out = new CPOperand(parts[2]);
    return new LocalCPInstruction(in, out, str);
}