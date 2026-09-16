/**
 * @see Command#doAction(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    JMeterTreeListener treeListener = GuiPackage.getInstance().getTreeListener();
    if (treeListener.getSelectedNodes().length != 1) {
        return;
    }
    JMeterTreeNode currentNode = treeListener.getCurrentNode();
    JMeterTreeNode parentNode = getParentNode(currentNode);
    if (parentNode == null) {
        return;
    }
    String action = e.getActionCommand();
    int index = parentNode.getIndex(currentNode);
    switch(action) {
        case ActionNames.MOVE_UP:
            handleMoveUp(currentNode, parentNode, index);
            break;
        case ActionNames.MOVE_DOWN:
            handleMoveDown(currentNode, parentNode, index);
            break;
        case ActionNames.MOVE_LEFT:
            handleMoveLeft(currentNode, parentNode);
            break;
        case ActionNames.MOVE_RIGHT:
            handleMoveRight(currentNode, parentNode);
            break;
        default:
            // Default case to comply with the Google Java Style Guide
            break;
    }
    GuiPackage.getInstance().getMainFrame().repaint();
}
// ---- helper method(s) introduced by the refactoring ----
private static void handleMoveUp(JMeterTreeNode currentNode, JMeterTreeNode parentNode, int index) {
    if (index > 0) {
        moveAndSelectNode(currentNode, parentNode, index - 1);
    }
}

private static void handleMoveDown(JMeterTreeNode currentNode, JMeterTreeNode parentNode, int index) {
    if (index < parentNode.getChildCount() - 1) {
        moveAndSelectNode(currentNode, parentNode, index + 1);
    }
}

private static void handleMoveLeft(JMeterTreeNode currentNode, JMeterTreeNode parentNode) {
    JMeterTreeNode parentParentNode = getParentNode(parentNode);
    if (parentParentNode != null && canAddTo(parentParentNode, currentNode)) {
        moveAndSelectNode(currentNode, parentParentNode, parentParentNode.getIndex(parentNode));
    }
}

private static void handleMoveRight(JMeterTreeNode currentNode, JMeterTreeNode parentNode) {
    JMeterTreeNode after = (JMeterTreeNode) parentNode.getChildAfter(currentNode);
    if (after != null && canAddTo(after, currentNode)) {
        moveAndSelectNode(currentNode, after, 0);
    }
}

