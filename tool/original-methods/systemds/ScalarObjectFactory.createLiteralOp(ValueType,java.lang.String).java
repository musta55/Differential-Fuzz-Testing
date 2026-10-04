public static LiteralOp createLiteralOp(ValueType vt, String value) {
    switch(vt) {
        case FP64:
            return new LiteralOp(Double.parseDouble(value));
        case INT64:
            return new LiteralOp(Long.parseLong(value));
        case BOOLEAN:
            return new LiteralOp(Boolean.parseBoolean(value));
        case STRING:
            return new LiteralOp(value);
        default:
            throw new RuntimeException("Unsupported scalar value type: " + vt.name());
    }
}