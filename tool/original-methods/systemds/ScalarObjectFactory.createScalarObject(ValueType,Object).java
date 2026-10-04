public static ScalarObject createScalarObject(ValueType vt, Object obj) {
    //TODO add new scalar object for extended type system
    switch(vt) {
        case BOOLEAN:
            return new BooleanObject((Boolean) obj);
        case INT64:
            return new IntObject((Long) obj);
        case INT32:
            return new IntObject((Integer) obj);
        case FP64:
            return new DoubleObject((Double) obj);
        case FP32:
            return new DoubleObject((Float) obj);
        case STRING:
            return new StringObject((String) obj);
        default:
            throw new RuntimeException("Unsupported scalar value type: " + vt.name());
    }
}