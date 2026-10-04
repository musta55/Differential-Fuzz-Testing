/**
 * Sets the ValueAt attribute of the Arguments object.
 *
 * @param value
 *            the new ValueAt value
 */
@Override
public void setValueAt(Object value, int row, int column) {
    if (isRowExists(row)) {
        model.setCurrentPos(row);
        model.addColumnValue(model.getHeaders()[column], value);
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

