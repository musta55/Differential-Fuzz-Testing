public static BuiltinUnaryGPUInstruction parseInstruction(String str) {
    CPOperand in = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    CPOperand out = new CPOperand("", ValueType.UNKNOWN, DataType.UNKNOWN);
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = null;
    //print or stop or cumulative aggregates
    if (parts.length == 4) {
        throw new DMLRuntimeException("The instruction is not supported on GPU:" + str);
    } else //2+1, general case
    {
        InstructionUtils.checkNumFields(str, 2);
        opcode = parts[0];
        in.split(parts[1]);
        out.split(parts[2]);
        if (in.getDataType() == DataType.SCALAR)
            throw new DMLRuntimeException("The instruction is not supported on GPU:" + str);
        else if (in.getDataType() == DataType.MATRIX)
            return new MatrixBuiltinGPUInstruction(null, in, out, opcode, str);
    }
    return null;
}