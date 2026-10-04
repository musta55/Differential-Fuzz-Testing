public static MatrixSketch get(Operator op) {
    if (op instanceof CountDistinctOperator) {
        CountDistinctOperator cdop = (CountDistinctOperator) op;
        if (cdop.getOperatorType() == CountDistinctOperatorTypes.COUNT) {
            return new CountDistinctFunctionSketch(op);
        } else if (cdop.getOperatorType() == CountDistinctOperatorTypes.KMV) {
            return new KMVSketch(op);
        } else {
            throw new NotImplementedException("Only COUNT and KMV count distinct sketches are supported for now");
        }
    } else {
        throw new IllegalArgumentException("Only sketches for count distinct operators are supported for now");
    }
}