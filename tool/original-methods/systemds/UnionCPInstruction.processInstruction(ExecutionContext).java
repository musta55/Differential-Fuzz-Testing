@Override
public void processInstruction(ExecutionContext ec) {
    MatrixBlock matBlock1 = ec.getMatrixInput(input1.getName());
    MatrixBlock matBlock2 = ec.getMatrixInput(input2.getName());
    MatrixBlock out = matBlock1.unionOperations(matBlock1, matBlock2);
    ec.releaseMatrixInput(input1.getName());
    ec.releaseMatrixInput(input2.getName());
    ec.setMatrixOutput(output.getName(), out);
}