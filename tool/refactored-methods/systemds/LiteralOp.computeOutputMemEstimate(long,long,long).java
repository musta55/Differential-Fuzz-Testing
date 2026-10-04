@Override
protected double computeOutputMemEstimate(long dim1, long dim2, long nnz) {
    switch(getValueType()) {
        case INT64:
            return OptimizerUtils.INT_SIZE;
        case FP64:
            return OptimizerUtils.DOUBLE_SIZE;
        case BOOLEAN:
            return OptimizerUtils.BOOLEAN_SIZE;
        case STRING:
            return this.value_string.length() * OptimizerUtils.CHAR_SIZE;
        case UNKNOWN:
            return OptimizerUtils.DEFAULT_SIZE;
        default:
            return 0;
    }
}
// ---- helper method(s) introduced by the refactoring ----
private Lop createLiteralLop() throws LopsException {
    switch(getValueType()) {
        case FP64:
            return Data.createLiteralLop(ValueType.FP64, Double.toString(value_double));
        case BOOLEAN:
            return Data.createLiteralLop(ValueType.BOOLEAN, Boolean.toString(value_boolean));
        case STRING:
            return Data.createLiteralLop(ValueType.STRING, value_string);
        case INT64:
            return Data.createLiteralLop(ValueType.INT64, Long.toString(value_long));
        default:
            throw new HopsException(this.printErrorLocation() + "unexpected value type constructing lops for LiteralOp.\n");
    }
}

private String getValueAsString() {
    switch(getValueType()) {
        case FP64:
            return Double.toString(value_double);
        case BOOLEAN:
            return Boolean.toString(value_boolean);
        case STRING:
            return value_string;
        case INT64:
            return Long.toString(value_long);
        default:
            return "";
    }
}

