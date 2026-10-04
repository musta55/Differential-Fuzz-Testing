@Override
public void processInstruction(ExecutionContext ec) {
    MatrixBlock matBlock1 = ec.getMatrixInput(input1.getName());
    MatrixBlock matBlock2 = ec.getMatrixInput(input2.getName());
    String outputName = output.getName();
    COVOperator covOp = (COVOperator) _optr;
    CmCovObject covObj = performCovarianceOperation(ec, matBlock1, matBlock2, covOp);
    double val = covObj.getRequiredResult(_optr);
    ec.setScalarOutput(outputName, new DoubleObject(val));
}
// ---- helper method(s) introduced by the refactoring ----
private CovarianceCPInstruction(Builder builder) {
    super(CPType.AggregateBinary, builder.operator, builder.input1, builder.input2, builder.input3, builder.output, builder.opcode, builder.instructionString);
}

private CmCovObject performCovarianceOperation(ExecutionContext ec, MatrixBlock matBlock1, MatrixBlock matBlock2, COVOperator covOp) {
    if (input3 == null) {
        return performUnweightedCovariance(ec, matBlock1, matBlock2, covOp);
    } else {
        return performWeightedCovariance(ec, matBlock1, matBlock2, covOp);
    }
}

private CmCovObject performUnweightedCovariance(ExecutionContext ec, MatrixBlock matBlock1, MatrixBlock matBlock2, COVOperator covOp) {
    CmCovObject covObj = matBlock1.covOperations(covOp, matBlock2);
    ec.releaseMatrixInput(input1.getName(), input2.getName());
    return covObj;
}

private CmCovObject performWeightedCovariance(ExecutionContext ec, MatrixBlock matBlock1, MatrixBlock matBlock2, COVOperator covOp) {
    MatrixBlock wtBlock = ec.getMatrixInput(input3.getName());
    CmCovObject covObj = matBlock1.covOperations(covOp, matBlock2, wtBlock);
    ec.releaseMatrixInput(input1.getName(), input2.getName(), input3.getName());
    return covObj;
}

