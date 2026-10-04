public static LiteralOp createLiteralOp(ValueType vt, String value) {
    switch(vt) {
        case FP64:
            return new LiteralOp(parseDouble(value));
        case INT64:
            return new LiteralOp(parseLong(value));
        case BOOLEAN:
            return new LiteralOp(Boolean.parseBoolean(value));
        case STRING:
            return new LiteralOp(value);
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

