/**
 * Remove the currently selected argument from the table.
 */
protected void deleteArgument() {
    GuiUtils.cancelEditing(table);
    int rowSelected = table.getSelectedRow();
    if (rowSelected >= 0) {
        removeSelectedRowFromTableModel(rowSelected);
        updateDeleteButtonStatus();
        selectAppropriateRowAfterDeletion(rowSelected);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void clearTableModel() {
    tableModel.clearData();
}

private void populateTableModel(TestElement el) {
    PropertyIterator iter = el.propertyIterator();
    while (iter.hasNext()) {
        JMeterProperty prop = iter.next();
        tableModel.addRow(new Object[] { prop.getName(), prop.getStringValue() });
    }
}

private void updateTestElementFromTableModel(TestElement el) {
    Data model = tableModel.getData();
    model.reset();
    while (model.next()) {
        el.setProperty(new StringProperty((String) model.getColumnValue(COLUMN_NAMES_0), (String) model.getColumnValue(COLUMN_NAMES_1)));
    }
}

private JButton createAddButton() {
    //$NON-NLS-1$
    JButton add = new JButton(JMeterUtils.getResString("add"));
    add.setActionCommand(ADD);
    add.addActionListener(this);
    add.setEnabled(true);
    return add;
}

private JButton createDeleteButton() {
    // $NON-NLS-1$
    JButton delete = new JButton(JMeterUtils.getResString("delete"));
    delete.setActionCommand(DELETE);
    delete.addActionListener(this);
    return delete;
}

private void addNewRowToTableModel() {
    tableModel.addNewRow();
    tableModel.fireTableDataChanged();
}

private void enableDeleteButton() {
    delete.setEnabled(true);
}

private void selectLastRowInTable() {
    int rowToSelect = tableModel.getRowCount() - 1;
    table.setRowSelectionInterval(rowToSelect, rowToSelect);
}

private void removeSelectedRowFromTableModel(int rowSelected) {
    tableModel.removeRow(rowSelected);
    tableModel.fireTableDataChanged();
}

private void updateDeleteButtonStatus() {
    delete.setEnabled(tableModel.getRowCount() > 0);
}

private void selectAppropriateRowAfterDeletion(int rowSelected) {
    if (tableModel.getRowCount() > 0) {
        int rowToSelect = Math.min(rowSelected, tableModel.getRowCount() - 1);
        table.setRowSelectionInterval(rowToSelect, rowToSelect);
    }
}

