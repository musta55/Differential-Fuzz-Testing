/**
 * Get all of the values from the GUI component and set them in the
 * TestElement.
 *
 * @param el
 *            the TestElement to modify
 */
@Override
public void modifyTestElement(TestElement el) {
    GuiUtils.stopTableEditing(table);
    Data model = tableModel.getData();
    model.reset();
    while (model.next()) {
        el.setProperty(new StringProperty((String) model.getColumnValue(COLUMN_NAMES_0), (String) model.getColumnValue(COLUMN_NAMES_1)));
    }
    super.configureTestElement(el);
}