public void addNewColumn(String colName, Class<?> colClass) {
    model.addHeader(colName);
    Class<?>[] newClasses = new Class[columnClasses.length + 1];
    System.arraycopy(columnClasses, 0, newClasses, 0, columnClasses.length);
    newClasses[newClasses.length - 1] = colClass;
    columnClasses = newClasses;
    Object defaultValue = createDefaultValue(columnClasses.length - 1);
    model.setColumnData(colName, defaultValue);
    this.fireTableStructureChanged();
}