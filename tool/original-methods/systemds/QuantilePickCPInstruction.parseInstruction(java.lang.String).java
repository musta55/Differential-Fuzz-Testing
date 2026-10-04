public static QuantilePickCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    if (!opcode.equalsIgnoreCase(Opcodes.QPICK.toString()))
        throw new DMLRuntimeException("Unknown opcode while parsing a QuantilePickCPInstruction: " + str);
    //instruction parsing
    if (parts.length == 4) {
        //instructions of length 4 originate from unary - mr-iqm
        //TODO this should be refactored to use pickvaluecount lops
        CPOperand in1 = new CPOperand(parts[1]);
        CPOperand in2 = new CPOperand(parts[2]);
        CPOperand out = new CPOperand(parts[3]);
        OperationTypes ptype = OperationTypes.IQM;
        boolean inmem = false;
        return new QuantilePickCPInstruction(null, in1, in2, out, ptype, inmem, opcode, str);
    } else if (parts.length == 5) {
        CPOperand in1 = new CPOperand(parts[1]);
        CPOperand out = new CPOperand(parts[2]);
        OperationTypes ptype = OperationTypes.valueOf(parts[3]);
        boolean inmem = Boolean.parseBoolean(parts[4]);
        return new QuantilePickCPInstruction(null, in1, out, ptype, inmem, opcode, str);
    } else if (parts.length == 6) {
        CPOperand in1 = new CPOperand(parts[1]);
        CPOperand in2 = new CPOperand(parts[2]);
        CPOperand out = new CPOperand(parts[3]);
        OperationTypes ptype = OperationTypes.valueOf(parts[4]);
        boolean inmem = Boolean.parseBoolean(parts[5]);
        return new QuantilePickCPInstruction(null, in1, in2, out, ptype, inmem, opcode, str);
    }
    return null;
}