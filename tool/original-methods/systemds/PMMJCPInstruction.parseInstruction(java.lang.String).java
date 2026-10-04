public static PMMJCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    InstructionUtils.checkNumFields(parts, 5);
    String opcode = parts[0];
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand in2 = new CPOperand(parts[2]);
    CPOperand in3 = new CPOperand(parts[3]);
    CPOperand out = new CPOperand(parts[4]);
    int k = Integer.parseInt(parts[5]);
    if (!opcode.equalsIgnoreCase(Opcodes.PMM.toString()))
        throw new DMLRuntimeException("Unknown opcode while parsing an PMMJCPInstruction: " + str);
    else
        return new PMMJCPInstruction(new Operator(true), in1, in2, in3, out, k, opcode, str);
}