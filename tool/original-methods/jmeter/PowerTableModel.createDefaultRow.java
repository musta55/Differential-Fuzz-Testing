private Object[] createDefaultRow() {
    Object[] rowData = new Object[getColumnCount()];
    for (int i = 0; i < rowData.length; i++) {
        rowData[i] = createDefaultValue(i);
    }
    return rowData;
}