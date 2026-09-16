/**
 * Enable or disable the delete button depending on whether or not there is
 * a row to be deleted.
 */
protected void checkDeleteStatus() {
    // Disable DELETE if there are no rows in the table to delete.
    if (tableModel.getRowCount() == 0) {
        delete.setEnabled(false);
    } else {
        delete.setEnabled(true);
    }
}