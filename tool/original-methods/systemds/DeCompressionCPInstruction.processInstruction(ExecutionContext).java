@Override
public void processInstruction(ExecutionContext ec) {
    // Get matrix block input
    MatrixBlock in = ec.getMatrixInput(input1.getName());
    MatrixBlock out = (in instanceof CompressedMatrixBlock) ? ((CompressedMatrixBlock) in).decompress(OptimizerUtils.getConstrainedNumThreads(-1)) : in;
    // MatrixBlock out = (in instanceof CompressedMatrixBlock) ? ((CompressedMatrixBlock) in)
    // 	.squeeze(OptimizerUtils.getConstrainedNumThreads(-1)) : in;
    ec.releaseMatrixInput(input1.getName());
    ec.setMatrixOutput(output.getName(), out);
}