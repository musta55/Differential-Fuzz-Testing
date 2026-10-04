@Override
public /**
 * Processes the decompression instruction.
 *
 * @param executionContext the execution context containing the matrix inputs and outputs
 */
void processInstruction(ExecutionContext executionContext) {
    // Get matrix block input
    MatrixBlock inputMatrix = executionContext.getMatrixInput(input1.getName());
    MatrixBlock outputMatrix = decompressMatrixBlock(inputMatrix);
    executionContext.releaseMatrixInput(input1.getName());
    executionContext.setMatrixOutput(output.getName(), outputMatrix);
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Decompresses a matrix block if it is an instance of CompressedMatrixBlock.
 *
 * @param matrixBlock the matrix block to potentially decompress
 * @return the decompressed matrix block if compressed, otherwise the original matrix block
 */
private MatrixBlock decompressMatrixBlock(MatrixBlock matrixBlock) {
    return (matrixBlock instanceof CompressedMatrixBlock) ? ((CompressedMatrixBlock) matrixBlock).decompress(OptimizerUtils.getConstrainedNumThreads(-1)) : matrixBlock;
}

