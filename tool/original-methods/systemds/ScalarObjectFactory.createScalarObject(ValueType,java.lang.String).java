public static ScalarObject createScalarObject(ValueType vt, String value) {
    switch(vt) {
        case INT64:
            return new IntObject(UtilFunctions.parseToLong(value));
        case FP64:
            return new DoubleObject(Double.parseDouble(value));
        case BOOLEAN:
            return new BooleanObject(Boolean.parseBoolean(value));
        case STRING:
            return new StringObject(value);
        default:
            throw new RuntimeException("Unsupported scalar value type: " + vt.name());
    }
}