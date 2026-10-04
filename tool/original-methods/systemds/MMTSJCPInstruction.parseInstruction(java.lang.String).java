public static MMTSJCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    InstructionUtils.checkNumFields(parts, 4);
    String opcode = parts[0];
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand out = new CPOperand(parts[2]);
    MMTSJType titype = MMTSJType.valueOf(parts[3]);
    int k = Integer.parseInt(parts[4]);
    if (!opcode.equalsIgnoreCase(Opcodes.TSMM.toString()))
        throw new DMLRuntimeException("Unknown opcode while parsing an MMTSJCPInstruction: " + str);
    else
        return new MMTSJCPInstruction(new Operator(true), in1, titype, out, k, opcode, str);
}