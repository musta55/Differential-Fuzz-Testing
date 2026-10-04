@Override
public void processInstruction(ExecutionContext ec) {
    switch(_type) {
        case VALUEPICK:
            if (//INMEM VALUEPICK
            _inmem) {
                MatrixBlock matBlock = ec.getMatrixInput(input1.getName());
                if (input2.getDataType() == DataType.SCALAR) {
                    ScalarObject quantile = ec.getScalarInput(input2);
                    //pick value w/ explicit averaging for even-length arrays
                    double picked = matBlock.pickValue(quantile.getDoubleValue(), matBlock.getLength() % 2 == 0);
                    ec.setScalarOutput(output.getName(), new DoubleObject(picked));
                } else {
                    MatrixBlock quantiles = ec.getMatrixInput(input2.getName());
                    //pick value w/ explicit averaging for even-length arrays
                    MatrixBlock resultBlock = matBlock.pickValues(quantiles, new MatrixBlock(), matBlock.getLength() % 2 == 0);
                    quantiles = null;
                    ec.releaseMatrixInput(input2.getName());
                    ec.setMatrixOutput(output.getName(), resultBlock);
                }
                ec.releaseMatrixInput(input1.getName());
            }
            break;
        case MEDIAN:
            if (//INMEM MEDIAN
            _inmem) {
                double picked = ec.getMatrixInput(input1.getName()).median();
                ec.setScalarOutput(output.getName(), new DoubleObject(picked));
                ec.releaseMatrixInput(input1.getName());
                break;
            }
            break;
        case IQM:
            if (//INMEM IQM
            _inmem) {
                MatrixBlock matBlock1 = ec.getMatrixInput(input1.getName());
                double iqm = matBlock1.interQuartileMean();
                ec.releaseMatrixInput(input1.getName());
                ec.setScalarOutput(output.getName(), new DoubleObject(iqm));
            }
            break;
        default:
            throw new DMLRuntimeException("Unsupported qpick operation type: " + _type);
    }
}