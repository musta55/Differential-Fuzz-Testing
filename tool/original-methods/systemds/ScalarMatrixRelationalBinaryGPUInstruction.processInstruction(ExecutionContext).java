@Override
public void processInstruction(ExecutionContext ec) {
    GPUStatistics.incrementNoOfExecutedGPUInst();
    CPOperand mat = (_input1.getDataType() == DataType.MATRIX) ? _input1 : _input2;
    CPOperand scalar = (_input1.getDataType() == DataType.MATRIX) ? _input2 : _input1;
    MatrixObject in1 = getMatrixInputForGPUInstruction(ec, mat.getName());
    ScalarObject constant = ec.getScalarInput(scalar);
    int rlen = (int) in1.getNumRows();
    int clen = (int) in1.getNumColumns();
    ec.setMetaData(_output.getName(), rlen, clen);
    ScalarOperator sc_op = (ScalarOperator) _optr;
    sc_op = sc_op.setConstant(constant.getDoubleValue());
    LibMatrixCUDA.matrixScalarRelational(ec, ec.getGPUContext(0), getExtendedOpcode(), in1, _output.getName(), sc_op);
    ec.releaseMatrixInputForGPUInstruction(mat.getName());
    ec.releaseMatrixOutputForGPUInstruction(_output.getName());
}