@Override
public void processInstruction(ExecutionContext ec) {
    MatrixBlock leftMatrix = ec.getMatrixInput(input1.getName());
    MatrixBlock rightMatrix = ec.getMatrixInput(input2.getName());
    MatrixBlock out = leftMatrix.unionOperations(leftMatrix, rightMatrix);
    ec.releaseMatrixInput(input1.getName());
    ec.releaseMatrixInput(input2.getName());
    ec.setMatrixOutput(output.getName(), out);
}
// ---- helper method(s) introduced by the refactoring ----
private UnionCPInstruction(Builder builder) {
    super(CPType.Union, builder.operator, builder.in1, builder.in2, builder.out, builder.opcode, builder.istr);
}

