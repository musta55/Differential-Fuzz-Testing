@Override
public void addRow(Object[] data) {
    validateRowValues(data);
    model.setCurrentPos(model.size());
    for (int i = 0; i < data.length; i++) {
        model.addColumnValue(model.getHeaders()[i], data[i]);
    }
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

