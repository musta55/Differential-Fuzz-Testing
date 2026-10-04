@Override
public void processInstruction(ExecutionContext ec) {
    MatrixBlock matBlock1 = ec.getMatrixInput(input1.getName());
    MatrixBlock matBlock2 = ec.getMatrixInput(input2.getName());
    String output_name = output.getName();
    COVOperator cov_op = (COVOperator) _optr;
    CmCovObject covobj = null;
    if (input3 == null) {
        // Unweighted: cov.mvar0.mvar1.out
        covobj = matBlock1.covOperations(cov_op, matBlock2);
        ec.releaseMatrixInput(input1.getName(), input2.getName());
    } else {
        // Weighted: cov.mvar0.mvar1.weights.out
        MatrixBlock wtBlock = ec.getMatrixInput(input3.getName());
        covobj = matBlock1.covOperations(cov_op, matBlock2, wtBlock);
        ec.releaseMatrixInput(input1.getName(), input2.getName(), input3.getName());
    }
    double val = covobj.getRequiredResult(_optr);
    ec.setScalarOutput(output_name, new DoubleObject(val));
}