/**
 * Parses a string representation of a decompression instruction.
 *
 * @param instructionString the string representation of the instruction
 * @return a new instance of DeCompressionCPInstruction
 */
public static DeCompressionCPInstruction parseInstruction(String instructionString) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(instructionString);
    String opcode = parts[0];
    CPOperand inputOperand = new CPOperand(parts[1]);
    CPOperand outputOperand = new CPOperand(parts[2]);
    return new DeCompressionCPInstruction(null, inputOperand, outputOperand, opcode, instructionString);
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

