@Override
public void processInstruction(ExecutionContext ec) {
    // get inputs
    MatrixBlock matBlock1 = ec.getMatrixInput(input1.getName());
    MatrixBlock matBlock2 = ec.getMatrixInput(input2.getName());
    validateInput(matBlock1, matBlock2);
    MatrixBlock ret;
    if (_type == AppendType.CBIND && (matBlock1 instanceof CompressedMatrixBlock || matBlock2 instanceof CompressedMatrixBlock))
        ret = CLALibCBind.cbind(matBlock1, matBlock2, InfrastructureAnalyzer.getLocalParallelism());
    else
        ret = matBlock1.append(matBlock2, new MatrixBlock(), _type == AppendType.CBIND);
    ec.setMatrixOutput(output.getName(), ret);
    ec.releaseMatrixInput(input1.getName(), input2.getName());
}