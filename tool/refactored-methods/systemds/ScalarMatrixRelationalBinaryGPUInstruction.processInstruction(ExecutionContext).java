@Override
public void processInstruction(ExecutionContext ec) {
    GPUStatistics.incrementNoOfExecutedGPUInst();
    CPOperand matrixOperand = (_input1.getDataType() == DataType.MATRIX) ? _input1 : _input2;
    CPOperand scalarOperand = (_input1.getDataType() == DataType.MATRIX) ? _input2 : _input1;
    MatrixObject matrixInput = getMatrixInputForGPUInstruction(ec, matrixOperand.getName());
    ScalarObject scalarInput = ec.getScalarInput(scalarOperand);
    int numRows = (int) matrixInput.getNumRows();
    int numCols = (int) matrixInput.getNumColumns();
    ec.setMetaData(_output.getName(), numRows, numCols);
    ScalarOperator scalarOperator = (ScalarOperator) _optr;
    scalarOperator.setConstant(scalarInput.getDoubleValue());
    LibMatrixCUDA.matrixScalarRelational(ec, ec.getGPUContext(0), getExtendedOpcode(), matrixInput, _output.getName(), scalarOperator);
    ec.releaseMatrixInputForGPUInstruction(matrixOperand.getName());
    ec.releaseMatrixOutputForGPUInstruction(_output.getName());
}