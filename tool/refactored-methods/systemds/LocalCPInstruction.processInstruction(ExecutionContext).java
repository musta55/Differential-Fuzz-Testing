@Override
public void processInstruction(ExecutionContext ec) {
    MatrixBlock in = ec.getMatrixInput(input1.getName());
    ec.releaseMatrixInput(input1.getName());
    ec.setMatrixOutput(output.getName(), in);
}