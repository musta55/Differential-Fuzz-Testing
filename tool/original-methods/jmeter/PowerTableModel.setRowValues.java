public void setRowValues(int row, Object[] values) {
    if (values.length != model.getHeaderCount()) {
        throw new IllegalArgumentException("Incorrect number of data items");
    }
    model.setCurrentPos(row);
    for (int i = 0; i < values.length; i++) {
        model.addColumnValue(model.getHeaders()[i], values[i]);
    }
}