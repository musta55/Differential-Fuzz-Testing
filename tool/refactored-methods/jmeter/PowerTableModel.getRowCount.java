/**
 * Required by table model interface.
 *
 * @return the RowCount value
 */
@Override
public int getRowCount() {
    return model == null ? 0 : model.size();
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

