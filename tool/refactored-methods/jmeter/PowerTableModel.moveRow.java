@Override
public void moveRow(int start, int end, int to) {
    List<Object[]> rows = new ArrayList<>();
    for (int i = 0; i < getRowCount(); i++) {
        rows.add(getRowData(i));
    }
    List<Object[]> subList = new ArrayList<>(rows.subList(start, end));
    rows.subList(start, end).clear();
    rows.addAll(to, subList);
    for (int i = 0; i < rows.size(); i++) {
        setRowValues(i, rows.get(i));
    }
    super.fireTableChanged(new TableModelEvent(this));
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

