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
    Builder builder = new Builder().setIn1(in1).setIn2(in2).setIn3(in3).setOut(out).setOpcode(opcode).setStr(str).setNumThreads(numThreads);
    return builder.build();
}
// ---- helper method(s) introduced by the refactoring ----
private int determineCentralMomentOrder(ScalarObject order) {
    try {
        return (int) order.getLongValue();
    } catch (NumberFormatException e) {
        // unknown at compilation time
        return -1;
    }
}

