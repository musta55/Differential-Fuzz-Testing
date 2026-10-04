@Override
public void processInstruction(ExecutionContext ec) {
    // Read input frame
    FrameBlock inBlock1 = ec.getFrameInput(config.in1.getName());
    // the vector with valid column lengths
    MatrixBlock featurelength = ec.getMatrixInput(config.in2.getName());
    // identify columns with invalid lengths
    FrameBlock out = inBlock1.invalidByLength(featurelength);
    // Release the memory occupied by inputs
    ec.releaseFrameInput(config.in1.getName());
    ec.releaseMatrixInput(config.in2.getName());
    // Attach result frame with output
    ec.setFrameOutput(config.out.getName(), out);
}