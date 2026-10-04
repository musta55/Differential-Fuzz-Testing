public static ReorgGPUInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    InstructionUtils.checkNumFields(parts, 2);
    String opcode = parts[0];
    CPOperand in = new CPOperand(parts[1]);
    CPOperand out = new CPOperand(parts[2]);
    if (!(opcode.equalsIgnoreCase("r'")))
        throw new DMLRuntimeException("Unknown opcode while parsing a ReorgInstruction: " + str);
    else
        return new ReorgGPUInstruction(new ReorgOperator(SwapIndex.getSwapIndexFnObject()), in, out, opcode, str);
}