public Object getValueAsObject() {
    String type = getType();
    String value = getValue();
    Function<String, Object> converter = TYPE_CONVERSION_MAP.get(type);
    return converter != null ? converter.apply(value) : null;
}