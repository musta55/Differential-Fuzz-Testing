public void addNewColumn(String colName, Class<?> colClass) {
    model.addHeader(colName);
    columnClasses = resizeArray(columnClasses, colClass);
    Object defaultValue = createDefaultValue(columnClasses.length - 1);
    model.setColumnData(colName, defaultValue);
    fireTableStructureChanged();
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

