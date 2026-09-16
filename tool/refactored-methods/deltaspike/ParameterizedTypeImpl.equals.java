@Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (!(obj instanceof ParameterizedType)) {
        return false;
    }
    ParameterizedType that = (ParameterizedType) obj;
    return ownerTypesEqual(that) && rawTypesEqual(that) && actualTypeArgumentsEqual(that);
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

