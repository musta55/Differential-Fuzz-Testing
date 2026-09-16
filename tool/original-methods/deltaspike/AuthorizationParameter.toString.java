public String toString() {
    StringBuilder builder = new StringBuilder();
    for (Map.Entry<Class<? extends Annotation>, Map<Method, Object>> bindingEntry : bindings.entrySet()) {
        builder.append('@').append(bindingEntry.getKey().getName()).append('(');
        for (Map.Entry<Method, Object> value : bindingEntry.getValue().entrySet()) {
            builder.append(value.getKey().getName()).append('=').append(value.getValue()).append(',');
        }
        if (bindingEntry.getValue().isEmpty()) {
            builder.append(')');
        } else {
            builder.setCharAt(builder.length() - 1, ')');
        }
    }
    if (!bindings.isEmpty()) {
        builder.append(' ');
    }
    builder.append(type);
    return builder.toString();
}