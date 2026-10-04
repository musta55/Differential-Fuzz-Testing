public static CentralMomentCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    //check supported opcode
    if (!opcode.equalsIgnoreCase(Opcodes.CM.toString())) {
        throw new DMLRuntimeException("Unsupported opcode " + opcode);
    }
    //w/o opcode
    InstructionUtils.checkNumFields(str, 4, 5);
    //data
    CPOperand in1 = new CPOperand(parts[1]);
    //scalar
    CPOperand in2 = new CPOperand(parts[2]);
    //weights
    CPOperand in3 = (parts.length == 5) ? null : new CPOperand(parts[3]);
    CPOperand out = new CPOperand(parts[parts.length - 2]);
    int numThreads = Integer.parseInt(parts[parts.length - 1]);
    /* 
		 * Exact order of the central moment MAY NOT be known at compilation time.
		 * We first try to parse the second argument as an integer, and if we fail, 
		 * we simply pass -1 so that getCMAggOpType() picks up AggregateOperationTypes.INVALID.
		 * It must be updated at run time in processInstruction() method.
		 */
    int cmOrder;
    try {
        cmOrder = Integer.parseInt((in3 == null) ? in2.getName() : in3.getName());
    } catch (NumberFormatException e) {
        // unknown at compilation time
        cmOrder = -1;
    }
    AggregateOperationTypes opType = CMOperator.getCMAggOpType(cmOrder);
    CMOperator cm = new CMOperator(CM.getCMFnObject(opType), opType, numThreads);
    return new CentralMomentCPInstruction(cm, in1, in2, in3, out, opcode, str);
}