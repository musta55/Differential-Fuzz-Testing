@Override
public String getOpString() {
    String val = null;
    switch(getValueType()) {
        case FP64:
            val = Double.toString(value_double);
            break;
        case BOOLEAN:
            val = Boolean.toString(value_boolean);
            break;
        case STRING:
            val = value_string;
            break;
        case INT64:
            val = Long.toString(value_long);
            break;
        default:
            val = "";
    }
    return "LiteralOp " + val;
}