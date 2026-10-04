public static ScalarObject createScalarObject(ValueType vt, Object obj) {
    //TODO add new scalar object for extended type system
    switch(vt) {
        case BOOLEAN:
            return new BooleanObject((Boolean) obj);
        case INT64:
            return new IntObject(((Number) obj).longValue());
        case INT32:
            return new IntObject(((Number) obj).intValue());
        case FP64:
            return new DoubleObject(((Number) obj).doubleValue());
        case FP32:
            return new DoubleObject(((Number) obj).floatValue());
        case STRING:
            return new StringObject((String) obj);
        default:
            throw new RuntimeException("Unsupported scalar value type: " + vt.name());
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static long parseLong(String value) {
    return UtilFunctions.parseToLong(value);
}

private static double parseDouble(String value) {
    return Double.parseDouble(value);
}

