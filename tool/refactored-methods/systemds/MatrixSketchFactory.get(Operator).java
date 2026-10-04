public static MatrixSketch get(Operator op) {
    if (op instanceof CountDistinctOperator) {
        CountDistinctOperator cdop = (CountDistinctOperator) op;
        return getCountDistinctSketch(cdop);
    } else {
        throw new IllegalArgumentException("Only sketches for count distinct operators are supported for now");
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static MatrixSketch getCountDistinctSketch(CountDistinctOperator cdop) {
    switch(cdop.getOperatorType()) {
        case COUNT:
            return new CountDistinctFunctionSketch(cdop);
        case KMV:
            return new KMVSketch(cdop);
        default:
            throw new NotImplementedException("Only COUNT and KMV count distinct sketches are supported for now");
    }
}

