private Object createDefaultValue(int index) {
    Class<?> colClass = getColumnClass(index);
    try {
        return colClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
        for (Object initArg : DEFAULT_ARGS) {
            try {
                Constructor<?> constr = colClass.getConstructor(initArg.getClass());
                return constr.newInstance(initArg);
            } catch (ReflectiveOperationException ignored) {
                // no need to log this, as we are just trying out all available default args
            }
        }
    }
    return "";
}
// ---- helper method(s) introduced by the refactoring ----
private void validateRowValues(Object[] values) {
    if (values.length != model.getHeaderCount()) {
        throw new IllegalArgumentException("Incorrect number of data items");
    }
}

private static Class<?>[] resizeArray(Class<?>[] original, Class<?> newElement) {
    Class<?>[] newArray = Arrays.copyOf(original, original.length + 1);
    newArray[newArray.length - 1] = newElement;
    return newArray;
}

private boolean isRowExists(int row) {
    return row < model.size();
}

