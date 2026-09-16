@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(rawType);
    if (actualTypeArguments.length > 0) {
        sb.append("<");
        sb.append(String.join(",", Arrays.stream(actualTypeArguments).map(Type::toString).toArray(String[]::new)));
        sb.append(">");
    }
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private boolean ownerTypesEqual(ParameterizedType that) {
    return Objects.equals(ownerType, that.getOwnerType());
}

private boolean rawTypesEqual(ParameterizedType that) {
    return Objects.equals(rawType, that.getRawType());
}

private boolean actualTypeArgumentsEqual(ParameterizedType that) {
    return Arrays.equals(actualTypeArguments, that.getActualTypeArguments());
}

