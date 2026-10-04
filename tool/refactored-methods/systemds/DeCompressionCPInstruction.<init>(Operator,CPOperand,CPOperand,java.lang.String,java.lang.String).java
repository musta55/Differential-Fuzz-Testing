private DeCompressionCPInstruction(Operator operator, CPOperand inputOperand, CPOperand outputOperand, String operationCode, String instructionString) {
    super(CPType.Compression, operator, inputOperand, null, null, outputOperand, operationCode, instructionString);
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

