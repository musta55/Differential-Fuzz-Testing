public static ScalarObject createScalarObject(ValueType vt, String value) {
    switch(vt) {
        case INT64:
            return new IntObject(parseLong(value));
        case FP64:
            return new DoubleObject(parseDouble(value));
        case BOOLEAN:
            return new BooleanObject(Boolean.parseBoolean(value));
        case STRING:
            return new StringObject(value);
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

