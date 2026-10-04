/**
 * Adds the specified class to the current node of the tree.
 */
@Override
public void doAction(ActionEvent e) {
    GuiPackage guiPackage = GuiPackage.getInstance();
    try {
        guiPackage.updateCurrentNode();
        TestElement testElement = guiPackage.createTestElement(((JComponent) e.getSource()).getName());
        JMeterTreeNode parentNode = guiPackage.getCurrentNode();
        JMeterTreeNode node = guiPackage.getTreeModel().addComponent(testElement, parentNode);
        guiPackage.getNamingPolicy().nameOnCreation(node);
        guiPackage.getMainFrame().getTree().setSelectionPath(new TreePath(node.getPath()));
    } catch (Exception err) {
        handleException(err);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void handleException(Exception err) {
    // $NON-NLS-1$
    log.error("Exception while adding a component to tree.", err);
    String msg = err.getMessage();
    if (msg == null) {
        msg = err.toString();
    }
    JMeterUtils.reportErrorToUser(msg);
}

