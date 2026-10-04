/**
 * This method performs the actual command processing.
 *
 * @param e
 *            the generic UI action event
 */
@Override
public void doAction(ActionEvent e) {
    boolean collapse = ActionNames.COLLAPSE_ALL.equals(e.getActionCommand());
    GuiPackage guiInstance = GuiPackage.getInstance();
    JTree jTree = guiInstance.getMainFrame().getTree();
    if (collapse) {
        for (int i = jTree.getRowCount() - 1; i >= 0; i--) {
            jTree.collapseRow(i);
        }
        return;
    }
    for (int i = 0; i < jTree.getRowCount(); i++) {
        jTree.expandRow(i);
    }
}