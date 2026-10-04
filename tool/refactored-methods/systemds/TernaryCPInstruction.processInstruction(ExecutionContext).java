@Override
public void processInstruction(ExecutionContext ec) {
    if (input1.isMatrix() || input2.isMatrix() || input3.isMatrix()) {
        processMatrixInstruction(ec);
    } else {
        processScalarInstruction(ec);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void processMatrixInstruction(ExecutionContext ec) {
    MatrixBlock m1 = getMatrixOrScalarAsMatrixBlock(input1, ec);
    MatrixBlock m2 = getMatrixOrScalarAsMatrixBlock(input2, ec);
    MatrixBlock m3 = getMatrixOrScalarAsMatrixBlock(input3, ec);
    MatrixBlock out = m1.ternaryOperations((TernaryOperator) _optr, m2, m3, new MatrixBlock());
    releaseInputsAndSetOutput(ec, m1, m2, m3, out);
}

private MatrixBlock getMatrixOrScalarAsMatrixBlock(CPOperand input, ExecutionContext ec) {
    return input.isMatrix() ? ec.getMatrixInput(input.getName()) : new MatrixBlock(ec.getScalarInput(input).getDoubleValue());
}

private void releaseInputsAndSetOutput(ExecutionContext ec, MatrixBlock m1, MatrixBlock m2, MatrixBlock m3, MatrixBlock out) {
    if (input1.isMatrix())
        ec.releaseMatrixInput(input1.getName());
    if (input2.isMatrix())
        ec.releaseMatrixInput(input2.getName());
    if (input3.isMatrix())
        ec.releaseMatrixInput(input3.getName());
    ec.setMatrixOutput(output.getName(), out);
}

private void processScalarInstruction(ExecutionContext ec) {
    if (((TernaryOperator) _optr).fn instanceof IfElse && output.getValueType() == ValueType.STRING) {
        String value = (ec.getScalarInput(input1).getDoubleValue() != 0 ? ec.getScalarInput(input2) : ec.getScalarInput(input3)).getStringValue();
        ec.setScalarOutput(output.getName(), new StringObject(value));
    } else {
        double value = ((TernaryOperator) _optr).fn.execute(ec.getScalarInput(input1).getDoubleValue(), ec.getScalarInput(input2).getDoubleValue(), ec.getScalarInput(input3).getDoubleValue());
        ec.setScalarOutput(output.getName(), ScalarObjectFactory.createScalarObject(output.getValueType(), value));
    }
}

