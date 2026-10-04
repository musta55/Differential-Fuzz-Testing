public static MMTSJGPUInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    InstructionUtils.checkNumFields(parts, 3);
    String opcode = parts[0];
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand out = new CPOperand(parts[2]);
    MMTSJType titype = MMTSJType.valueOf(parts[3]);
    if (!opcode.equalsIgnoreCase("tsmm"))
        throw new DMLRuntimeException("Unknown opcode while parsing an MMTSJGPUInstruction: " + str);
    else
        return new MMTSJGPUInstruction(new Operator(true), in1, titype, out, opcode, str);
}