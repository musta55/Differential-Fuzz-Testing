/**
 * Sets the ValueAt attribute of the Arguments object.
 *
 * @param value
 *            the new ValueAt value
 */
@Override
public void setValueAt(Object value, int row, int column) {
    if (row < model.size()) {
        model.setCurrentPos(row);
        model.addColumnValue(model.getHeaders()[column], value);
    }
}