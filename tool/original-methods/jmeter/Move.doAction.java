/**
 * @see Command#doAction(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    JMeterTreeListener treeListener = GuiPackage.getInstance().getTreeListener();
    if (treeListener.getSelectedNodes().length != 1) {
        // we can only move a single node
        return;
    }
    JMeterTreeNode currentNode = treeListener.getCurrentNode();
    JMeterTreeNode parentNode = getParentNode(currentNode);
    if (parentNode != null) {
        String action = e.getActionCommand();
        int index = parentNode.getIndex(currentNode);
        if (ActionNames.MOVE_UP.equals(action)) {
            if (index > 0) {
                // we stay within the same parent node
                int newIndx = index - 1;
                moveAndSelectNode(currentNode, parentNode, newIndx);
            }
        } else if (ActionNames.MOVE_DOWN.equals(action)) {
            if (index < parentNode.getChildCount() - 1) {
                // we stay within the same parent node
                int newIndx = index + 1;
                moveAndSelectNode(currentNode, parentNode, newIndx);
            }
        } else if (ActionNames.MOVE_LEFT.equals(action)) {
            JMeterTreeNode parentParentNode = getParentNode(parentNode);
            // move to the parent
            if (parentParentNode != null && canAddTo(parentParentNode, currentNode)) {
                moveAndSelectNode(currentNode, parentParentNode, parentParentNode.getIndex(parentNode));
            }
        } else if (ActionNames.MOVE_RIGHT.equals(action)) {
            JMeterTreeNode after = (JMeterTreeNode) parentNode.getChildAfter(currentNode);
            if (after != null && canAddTo(after, currentNode)) {
                // move as a child of the next sibling
                moveAndSelectNode(currentNode, after, 0);
            }
        }
    }
    GuiPackage.getInstance().getMainFrame().repaint();
}