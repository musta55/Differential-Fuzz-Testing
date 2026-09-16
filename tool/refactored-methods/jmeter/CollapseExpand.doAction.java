/**
 * This method performs the actual command processing.
 *
 * @param e
 *            the generic UI action event
 */
@Override
public void doAction(ActionEvent e) {
    if (ActionNames.COLLAPSE_ALL.equals(e.getActionCommand())) {
        collapseAll();
    } else {
        expandAll();
    }
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Collapses all rows in the JTree.
 */
private static void collapseAll() {
    GuiPackage guiInstance = GuiPackage.getInstance();
    JTree jTree = guiInstance.getMainFrame().getTree();
    for (int i = jTree.getRowCount() - 1; i >= 0; i--) {
        jTree.collapseRow(i);
    }
}

/**
 * Expands all rows in the JTree.
 */
private static void expandAll() {
    GuiPackage guiInstance = GuiPackage.getInstance();
    JTree jTree = guiInstance.getMainFrame().getTree();
    for (int i = 0; i < jTree.getRowCount(); i++) {
        jTree.expandRow(i);
    }
}

