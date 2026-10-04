/**
 * Remove the currently selected argument from the table.
 */
protected void deleteArgument() {
    // If a table cell is being edited, we must cancel the editing before
    // deleting the row
    GuiUtils.cancelEditing(table);
    int rowSelected = table.getSelectedRow();
    if (rowSelected >= 0) {
        tableModel.removeRow(rowSelected);
        tableModel.fireTableDataChanged();
        // Disable DELETE if there are no rows in the table to delete.
        if (tableModel.getRowCount() == 0) {
            delete.setEnabled(false);
        } else {
            // Table still contains one or more rows, so highlight (select)
            // the appropriate one.
            int rowToSelect = rowSelected;
            if (rowSelected >= tableModel.getRowCount()) {
                rowToSelect = rowSelected - 1;
            }
            table.setRowSelectionInterval(rowToSelect, rowToSelect);
        }
    }
}