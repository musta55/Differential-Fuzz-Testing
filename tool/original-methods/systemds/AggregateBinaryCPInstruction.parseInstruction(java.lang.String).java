public static AggregateBinaryCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    if (!opcode.equalsIgnoreCase(Opcodes.MMULT.toString())) {
        throw new DMLRuntimeException("AggregateBinaryInstruction.parseInstruction():: Unknown opcode " + opcode);
    }
    int numFields = InstructionUtils.checkNumFields(parts, 4, 6);
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand in2 = new CPOperand(parts[2]);
    CPOperand out = new CPOperand(parts[3]);
    int k = Integer.parseInt(parts[4]);
    AggregateBinaryOperator op = InstructionUtils.getMatMultOperator(k);
    if (numFields == 6) {
        boolean lt = Boolean.parseBoolean(parts[5]);
        boolean rt = Boolean.parseBoolean(parts[6]);
        return new AggregateBinaryCPInstruction(op, in1, in2, out, opcode, str, lt, rt);
    }
    return new AggregateBinaryCPInstruction(op, in1, in2, out, opcode, str);
}