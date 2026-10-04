@Override
public Lop constructLops() {
    //return already created lops
    if (getLops() != null)
        return getLops();
    try {
        Lop l = null;
        switch(getValueType()) {
            case FP64:
                l = Data.createLiteralLop(ValueType.FP64, Double.toString(value_double));
                break;
            case BOOLEAN:
                l = Data.createLiteralLop(ValueType.BOOLEAN, Boolean.toString(value_boolean));
                break;
            case STRING:
                l = Data.createLiteralLop(ValueType.STRING, value_string);
                break;
            case INT64:
                l = Data.createLiteralLop(ValueType.INT64, Long.toString(value_long));
                break;
            default:
                throw new HopsException(this.printErrorLocation() + "unexpected value type constructing lops for LiteralOp.\n");
        }
        l.getOutputParameters().setDimensions(0, 0, 0, -1);
        setLineNumbers(l);
        setLops(l);
    } catch (LopsException e) {
        throw new HopsException(e);
    }
    //note: no reblock lop because always scalar
    return getLops();
}