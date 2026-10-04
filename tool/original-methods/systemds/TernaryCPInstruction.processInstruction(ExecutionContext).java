@Override
public void processInstruction(ExecutionContext ec) {
    if (input1.isMatrix() || input2.isMatrix() || input3.isMatrix()) {
        //get all inputs as matrix blocks
        MatrixBlock m1 = input1.isMatrix() ? ec.getMatrixInput(input1.getName()) : new MatrixBlock(ec.getScalarInput(input1).getDoubleValue());
        MatrixBlock m2 = input2.isMatrix() ? ec.getMatrixInput(input2.getName()) : new MatrixBlock(ec.getScalarInput(input2).getDoubleValue());
        MatrixBlock m3 = input3.isMatrix() ? ec.getMatrixInput(input3.getName()) : new MatrixBlock(ec.getScalarInput(input3).getDoubleValue());
        //execution
        MatrixBlock out = m1.ternaryOperations((TernaryOperator) _optr, m2, m3, new MatrixBlock());
        //release the inputs and output
        if (input1.isMatrix())
            ec.releaseMatrixInput(input1.getName());
        if (input2.isMatrix())
            ec.releaseMatrixInput(input2.getName());
        if (input3.isMatrix())
            ec.releaseMatrixInput(input3.getName());
        ec.setMatrixOutput(output.getName(), out);
    } else {
        //SCALARS
        if (((TernaryOperator) _optr).fn instanceof IfElse && output.getValueType() == ValueType.STRING) {
            String value = (ec.getScalarInput(input1).getDoubleValue() != 0 ? ec.getScalarInput(input2) : ec.getScalarInput(input3)).getStringValue();
            ec.setScalarOutput(output.getName(), new StringObject(value));
        } else {
            double value = ((TernaryOperator) _optr).fn.execute(ec.getScalarInput(input1).getDoubleValue(), ec.getScalarInput(input2).getDoubleValue(), ec.getScalarInput(input3).getDoubleValue());
            ec.setScalarOutput(output.getName(), ScalarObjectFactory.createScalarObject(output.getValueType(), value));
        }
    }
}