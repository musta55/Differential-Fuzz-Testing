@Override
public void processInstruction(ExecutionContext ec) {
    // get inputs
    MatrixBlock matBlock1 = ec.getMatrixInput(input1.getName());
    MatrixBlock matBlock2 = ec.getMatrixInput(input2.getName());
    validateCBindInput(matBlock1, matBlock2);
    validateRBindInput(matBlock1, matBlock2);
    MatrixBlock ret;
    if (_type == AppendType.CBIND && (matBlock1 instanceof CompressedMatrixBlock || matBlock2 instanceof CompressedMatrixBlock))
        ret = CLALibCBind.cbind(matBlock1, matBlock2, InfrastructureAnalyzer.getLocalParallelism());
    else
        ret = matBlock1.append(matBlock2, new MatrixBlock(), _type == AppendType.CBIND);
    ec.setMatrixOutput(output.getName(), ret);
    ec.releaseMatrixInput(input1.getName(), input2.getName());
}
// ---- helper method(s) introduced by the refactoring ----
private void validateCBindInput(MatrixBlock m1, MatrixBlock m2) {
    if (_type == AppendType.CBIND && m1.getNumRows() != m2.getNumRows()) {
        throw new DMLRuntimeException("Append-cbind is not possible for input matrices " + input1.getName() + " and " + input2.getName() + " with different number of rows: " + m1.getNumRows() + " vs " + m2.getNumRows());
    }
}

private void validateRBindInput(MatrixBlock m1, MatrixBlock m2) {
    if (_type == AppendType.RBIND && m1.getNumColumns() != m2.getNumColumns()) {
        throw new DMLRuntimeException("Append-rbind is not possible for input matrices " + input1.getName() + " and " + input2.getName() + " with different number of columns: " + m1.getNumColumns() + " vs " + m2.getNumColumns());
    }
}

