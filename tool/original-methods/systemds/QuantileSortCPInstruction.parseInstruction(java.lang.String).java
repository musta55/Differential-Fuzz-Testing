public static QuantileSortCPInstruction parseInstruction(String str) {
    CPOperand in1 = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    CPOperand in2 = null;
    CPOperand out = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    if (opcode.equalsIgnoreCase(SortKeys.OPCODE)) {
        //#threads
        int k = Integer.parseInt(parts[parts.length - 1]);
        if (parts.length == 4) {
            // Example: sort:mVar1:mVar2 (input=mVar1, output=mVar2)
            InstructionUtils.checkNumFields(str, 3);
            parseInstruction(str, in1, null, out);
            return new QuantileSortCPInstruction(in1, out, opcode, str, k);
        } else if (parts.length == 5) {
            // Example: sort:mVar1:mVar2:mVar3 (input=mVar1, weights=mVar2, output=mVar3)
            InstructionUtils.checkNumFields(str, 4);
            in2 = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
            parseInstruction(str, in1, in2, out);
            return new QuantileSortCPInstruction(in1, in2, out, opcode, str, k);
        } else {
            throw new DMLRuntimeException("Invalid number of operands in instruction: " + str);
        }
    } else {
        throw new DMLRuntimeException("Unknown opcode while parsing a QuantileSortCPInstruction: " + str);
    }
}