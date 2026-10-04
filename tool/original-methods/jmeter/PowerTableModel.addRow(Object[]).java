@Override
public void addRow(Object[] data) {
    if (data.length != model.getHeaderCount()) {
        throw new IllegalArgumentException("Incorrect number of data items");
    }
    model.setCurrentPos(model.size());
    for (int i = 0; i < data.length; i++) {
        model.addColumnValue(model.getHeaders()[i], data[i]);
    }
}