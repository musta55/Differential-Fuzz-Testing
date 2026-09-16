@Override
public int hashCode() {
    return Arrays.hashCode(actualTypeArguments) ^ Objects.hashCode(ownerType) ^ Objects.hashCode(rawType);
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

