/**
 * Add a new argument row to the table.
 */
protected void addArgument() {
    // If a table cell is being edited, we should accept the current value
    // and stop the editing before adding a new row.
    GuiUtils.stopTableEditing(table);
    tableModel.addNewRow();
    tableModel.fireTableDataChanged();
    // Enable DELETE (which may already be enabled, but it won't hurt)
    delete.setEnabled(true);
    // Highlight (select) the appropriate row.
    int rowToSelect = tableModel.getRowCount() - 1;
    table.setRowSelectionInterval(rowToSelect, rowToSelect);
}