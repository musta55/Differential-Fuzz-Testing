@Override
public void processInstruction(ExecutionContext ec) {
    switch(_type) {
        case VALUEPICK:
            processValuePick(ec);
            break;
        case MEDIAN:
            processMedian(ec);
            break;
        case IQM:
            processIQM(ec);
            break;
        default:
            throw new DMLRuntimeException("Unsupported qpick operation type: " + _type);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static QuantilePickCPInstruction parseUnaryInstruction(String[] parts, String opcode, String str) {
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand in2 = new CPOperand(parts[2]);
    CPOperand out = new CPOperand(parts[3]);
    OperationTypes ptype = OperationTypes.IQM;
    boolean inmem = false;
    return new QuantilePickCPInstruction(null, in1, in2, out, ptype, inmem, opcode, str);
}

private static QuantilePickCPInstruction parseBinaryInstruction(String[] parts, String opcode, String str) {
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand out = new CPOperand(parts[2]);
    OperationTypes ptype = OperationTypes.valueOf(parts[3]);
    boolean inmem = Boolean.parseBoolean(parts[4]);
    return new QuantilePickCPInstruction(null, in1, out, ptype, inmem, opcode, str);
}

private static QuantilePickCPInstruction parseTernaryInstruction(String[] parts, String opcode, String str) {
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand in2 = new CPOperand(parts[2]);
    CPOperand out = new CPOperand(parts[3]);
    OperationTypes ptype = OperationTypes.valueOf(parts[4]);
    boolean inmem = Boolean.parseBoolean(parts[5]);
    return new QuantilePickCPInstruction(null, in1, in2, out, ptype, inmem, opcode, str);
}

private void processValuePick(ExecutionContext ec) {
    if (!_inmem)
        return;
    MatrixBlock matBlock = ec.getMatrixInput(input1.getName());
    if (input2.getDataType() == DataType.SCALAR) {
        ScalarObject quantile = ec.getScalarInput(input2);
        double picked = matBlock.pickValue(quantile.getDoubleValue(), matBlock.getLength() % 2 == 0);
        ec.setScalarOutput(output.getName(), new DoubleObject(picked));
    } else {
        MatrixBlock quantiles = ec.getMatrixInput(input2.getName());
        MatrixBlock resultBlock = matBlock.pickValues(quantiles, new MatrixBlock(), matBlock.getLength() % 2 == 0);
        quantiles = null;
        ec.releaseMatrixInput(input2.getName());
        ec.setMatrixOutput(output.getName(), resultBlock);
    }
    ec.releaseMatrixInput(input1.getName());
}

private void processMedian(ExecutionContext ec) {
    if (!_inmem)
        return;
    double picked = ec.getMatrixInput(input1.getName()).median();
    ec.setScalarOutput(output.getName(), new DoubleObject(picked));
    ec.releaseMatrixInput(input1.getName());
}

private void processIQM(ExecutionContext ec) {
    if (!_inmem)
        return;
    MatrixBlock matBlock1 = ec.getMatrixInput(input1.getName());
    double iqm = matBlock1.interQuartileMean();
    ec.releaseMatrixInput(input1.getName());
    ec.setScalarOutput(output.getName(), new DoubleObject(iqm));
}

