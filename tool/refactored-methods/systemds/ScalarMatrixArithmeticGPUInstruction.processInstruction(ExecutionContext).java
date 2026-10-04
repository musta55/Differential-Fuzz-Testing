@Override
public void processInstruction(ExecutionContext ec) {
    GPUStatistics.incrementNoOfExecutedGPUInst();
    CPOperand mat = getMatrixOperand();
    CPOperand scalar = getScalarOperand();
    MatrixObject in1 = getMatrixInputForGPUInstruction(ec, mat.getName());
    ScalarObject constant = ec.getScalarInput(scalar);
    int[] dimensions = getMatrixDimensions(in1, false);
    int rlen = dimensions[0];
    int clen = dimensions[1];
    ec.setMetaData(_output.getName(), rlen, clen);
    ScalarOperator sc_op = initializeScalarOperator(constant);
    LibMatrixCUDA.matrixScalarArithmetic(ec, ec.getGPUContext(0), getExtendedOpcode(), in1, _output.getName(), false, sc_op);
    ec.releaseMatrixInputForGPUInstruction(mat.getName());
    ec.releaseMatrixOutputForGPUInstruction(_output.getName());
}
// ---- helper method(s) introduced by the refactoring ----
private CPOperand getMatrixOperand() {
    return (_input1.getDataType() == DataType.MATRIX) ? _input1 : _input2;
}

private CPOperand getScalarOperand() {
    return (_input1.getDataType() == DataType.MATRIX) ? _input2 : _input1;
}

private int[] getMatrixDimensions(MatrixObject in1, boolean isTransposed) {
    int rlen = isTransposed ? (int) in1.getNumColumns() : (int) in1.getNumRows();
    int clen = isTransposed ? (int) in1.getNumRows() : (int) in1.getNumColumns();
    return new int[] { rlen, clen };
}

private ScalarOperator initializeScalarOperator(ScalarObject constant) {
    ScalarOperator sc_op = (ScalarOperator) _optr;
    return sc_op.setConstant(constant.getDoubleValue());
}

