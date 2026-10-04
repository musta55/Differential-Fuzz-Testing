@Override
public Lop constructLops() {
    //return already created lops
    if (getLops() != null)
        return getLops();
    try {
        Lop l = createLiteralLop();
        l.getOutputParameters().setDimensions(0, 0, 0, -1);
        setLineNumbers(l);
        setLops(l);
    } catch (LopsException e) {
        throw new HopsException(e);
    }
    //note: no reblock lop because always scalar
    return getLops();
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

