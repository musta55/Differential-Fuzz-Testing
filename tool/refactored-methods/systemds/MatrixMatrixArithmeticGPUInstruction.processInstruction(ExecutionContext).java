@Override
public void processInstruction(ExecutionContext ec) {
    GPUStatistics.incrementNoOfExecutedGPUInst();
    MatrixObject in1 = getMatrixInputForGPUInstruction(ec, config.in1.getName());
    MatrixObject in2 = getMatrixInputForGPUInstruction(ec, config.in2.getName());
    boolean isLeftTransposed = false;
    boolean isRightTransposed = false;
    long rlen = determineResultLength(in1, in2);
    long clen = determineResultWidth(in1, in2);
    ec.setMetaData(config.out.getName(), (int) rlen, (int) clen);
    BinaryOperator bop = (BinaryOperator) config.op;
    LibMatrixCUDA.matrixMatrixArithmetic(ec, ec.getGPUContext(0), getExtendedOpcode(), in1, in2, config.out.getName(), isLeftTransposed, isRightTransposed, bop);
    releaseInputsAndOutputs(ec, in1, in2);
}
// ---- helper method(s) introduced by the refactoring ----
protected MatrixMatrixArithmeticGPUInstruction(Config config) {
    super(config.op, config.in1, config.in2, config.out, config.opcode, config.istr);
    this.config = config;
}

private long determineResultLength(MatrixObject in1, MatrixObject in2) {
    long rlen1 = in1.getNumRows();
    long rlen2 = in2.getNumRows();
    return rlen1 != rlen2 ? Math.max(rlen1, rlen2) : rlen1;
}

private long determineResultWidth(MatrixObject in1, MatrixObject in2) {
    long clen1 = in1.getNumColumns();
    long clen2 = in2.getNumColumns();
    return clen1 != clen2 ? Math.max(clen1, clen2) : clen1;
}

private void releaseInputsAndOutputs(ExecutionContext ec, MatrixObject in1, MatrixObject in2) {
    ec.releaseMatrixInputForGPUInstruction(config.in1.getName());
    ec.releaseMatrixInputForGPUInstruction(config.in2.getName());
    ec.releaseMatrixOutputForGPUInstruction(config.out.getName());
}

