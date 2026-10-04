public Object getValueAsObject() {
    String type = getType();
    String value = getValue();
    if (type.equals(Boolean.class.getName())) {
        //NOSONAR
        return Boolean.valueOf(value);
    } else if (type.equals(Byte.class.getName())) {
        //NOSONAR
        return Byte.valueOf(value);
    } else if (type.equals(Short.class.getName())) {
        //NOSONAR
        return Short.valueOf(value);
    } else if (type.equals(Integer.class.getName())) {
        //NOSONAR
        return Integer.valueOf(value);
    } else if (type.equals(Long.class.getName())) {
        //NOSONAR
        return Long.valueOf(value);
    } else if (type.equals(Float.class.getName())) {
        //NOSONAR
        return Float.valueOf(value);
    } else if (type.equals(Double.class.getName())) {
        //NOSONAR
        return Double.valueOf(value);
    } else if (type.equals(String.class.getName())) {
        //NOSONAR
        return value;
    } else {
        return null;
    }
}