@Override
public void processInstruction(ExecutionContext ec) {
    // get input frames
    FrameBlock inBlock1 = ec.getFrameInput(input1.getName());
    FrameBlock inBlock2 = ec.getFrameInput(input2.getName());
    FrameBlock retBlock = performOperation(inBlock1, inBlock2);
    // Attach result frame with FrameBlock associated with output_name
    ec.setFrameOutput(output.getName(), retBlock);
    // Release the memory occupied by input frames
    ec.releaseFrameInput(input1.getName());
    ec.releaseFrameInput(input2.getName());
}
// ---- helper method(s) introduced by the refactoring ----
private FrameBlock performOperation(FrameBlock inBlock1, FrameBlock inBlock2) {
    String opcode = getOpcode();
    if (opcode.equals(Opcodes.DROPINVALIDTYPE.toString())) {
        return inBlock1.dropInvalidType(inBlock2);
    } else if (opcode.equals(Opcodes.VALUESWAP.toString())) {
        return inBlock1.valueSwap(inBlock2);
    } else if (opcode.equals(Opcodes.FREPLICATE.toString())) {
        return inBlock1.frameRowReplication(inBlock2);
    } else if (opcode.equals(Opcodes.APPLYSCHEMA.toString())) {
        int k = ((MultiThreadedOperator) _optr).getNumThreads();
        return FrameLibApplySchema.applySchema(inBlock1, inBlock2, k);
    } else {
        BinaryOperator dop = (BinaryOperator) _optr;
        return inBlock1.binaryOperations(dop, inBlock2, null);
    }
}

