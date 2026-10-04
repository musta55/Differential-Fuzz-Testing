/**
 * @see Command#doAction(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    JMeterTreeNode[] draggedNodes = Copy.getCopiedNodes();
    if (draggedNodes == null) {
        beep();
        return;
    }
    JMeterTreeListener treeListener = GuiPackage.getInstance().getTreeListener();
    JMeterTreeNode currentNode = treeListener.getCurrentNode();
    if (MenuFactory.canAddTo(currentNode, draggedNodes)) {
        Arrays.stream(draggedNodes).filter(Objects::nonNull).forEach(draggedNode -> addNode(currentNode, draggedNode));
    } else {
        beep();
    }
    GuiPackage.getInstance().getMainFrame().repaint();
}
// ---- helper method(s) introduced by the refactoring ----
private static void beep() {
    Toolkit.getDefaultToolkit().beep();
}

private static void handleException(IllegalUserActionException iuae) {
    // $NON-NLS-1$
    log.error("Illegal user action while adding a tree node.", iuae);
    JMeterUtils.reportErrorToUser(iuae.getMessage());
}

