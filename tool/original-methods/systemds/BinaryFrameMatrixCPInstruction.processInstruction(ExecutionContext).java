@Override
public void processInstruction(ExecutionContext ec) {
    // Read input frame
    FrameBlock inBlock1 = ec.getFrameInput(input1.getName());
    // the vector with valid column lengths
    MatrixBlock featurelength = ec.getMatrixInput(input2.getName());
    // identify columns with invalid lengths
    FrameBlock out = inBlock1.invalidByLength(featurelength);
    // Release the memory occupied by inputs
    ec.releaseFrameInput(input1.getName());
    ec.releaseMatrixInput(input2.getName());
    // Attach result frame with output
    ec.setFrameOutput(output.getName(), out);
}