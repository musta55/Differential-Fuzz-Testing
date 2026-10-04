public static CovarianceCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    if (!opcode.equalsIgnoreCase(Opcodes.COV.toString()))
        throw new DMLRuntimeException("CovarianceCPInstruction.parseInstruction():: Unknown opcode " + opcode);
    //w/o opcode
    InstructionUtils.checkNumFields(parts, 4, 5);
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand in2 = new CPOperand(parts[2]);
    CPOperand in3 = (parts.length == 5) ? null : new CPOperand(parts[3]);
    CPOperand out = new CPOperand(parts[parts.length - 2]);
    int numThreads = Integer.parseInt(parts[parts.length - 1]);
    COVOperator cov = new COVOperator(COV.getCOMFnObject(), numThreads);
    return new CovarianceCPInstruction(cov, in1, in2, in3, out, opcode, str);
}