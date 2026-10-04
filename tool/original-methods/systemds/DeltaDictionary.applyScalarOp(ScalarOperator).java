@Override
public DeltaDictionary applyScalarOp(ScalarOperator op) {
    if (op.fn instanceof Multiply || op.fn instanceof Divide) {
        final double[] retV = new double[_values.length];
        for (int i = 0; i < _values.length; i++) retV[i] = op.executeScalar(_values[i]);
        return new DeltaDictionary(retV, _numCols);
    } else {
        throw new NotImplementedException("Scalar op " + op.fn.getClass().getSimpleName() + " not supported in DeltaDictionary");
    }
}