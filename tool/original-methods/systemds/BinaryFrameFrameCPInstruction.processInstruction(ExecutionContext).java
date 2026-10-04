@Override
public void processInstruction(ExecutionContext ec) {
    // get input frames
    FrameBlock inBlock1 = ec.getFrameInput(input1.getName());
    FrameBlock inBlock2 = ec.getFrameInput(input2.getName());
    if (getOpcode().equals(Opcodes.DROPINVALIDTYPE.toString())) {
        // Perform computation using input frames, and produce the result frame
        FrameBlock retBlock = inBlock1.dropInvalidType(inBlock2);
        // Attach result frame with FrameBlock associated with output_name
        ec.setFrameOutput(output.getName(), retBlock);
    } else if (getOpcode().equals(Opcodes.VALUESWAP.toString())) {
        // Perform computation using input frames, and produce the result frame
        FrameBlock retBlock = inBlock1.valueSwap(inBlock2);
        // Attach result frame with FrameBlock associated with output_name
        ec.setFrameOutput(output.getName(), retBlock);
    } else if (getOpcode().equals(Opcodes.FREPLICATE.toString())) {
        // Perform computation using input frames, and produce the result frame
        FrameBlock retBlock = inBlock1.frameRowReplication(inBlock2);
        // Attach result frame with FrameBlock associated with output_name
        ec.setFrameOutput(output.getName(), retBlock);
    } else if (getOpcode().equals(Opcodes.APPLYSCHEMA.toString())) {
        final int k = ((MultiThreadedOperator) _optr).getNumThreads();
        final FrameBlock out = FrameLibApplySchema.applySchema(inBlock1, inBlock2, k);
        ec.setFrameOutput(output.getName(), out);
    } else {
        // Execute binary operations
        BinaryOperator dop = (BinaryOperator) _optr;
        FrameBlock outBlock = inBlock1.binaryOperations(dop, inBlock2, null);
        // Attach result frame with FrameBlock associated with output_name
        ec.setFrameOutput(output.getName(), outBlock);
    }
    // Release the memory occupied by input frames
    ec.releaseFrameInput(input1.getName());
    ec.releaseFrameInput(input2.getName());
}