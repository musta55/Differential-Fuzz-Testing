@Override
public void doAction(ActionEvent e) {
    GuiPackage instance = GuiPackage.getInstance();
    JMeterTreeListener treeListener = instance.getTreeListener();
    JMeterTreeNode[] copiedNodes = Copy.cloneTreeNodes(treeListener.getSelectedNodes());
    JMeterTreeNode currentNode = treeListener.getCurrentNode();
    JMeterTreeNode parentNode = (JMeterTreeNode) currentNode.getParent();
    JMeterTreeModel treeModel = instance.getTreeModel();
    for (int nodeIndex = copiedNodes.length - 1; nodeIndex >= 0; nodeIndex--) {
        JMeterTreeNode copiedNode = copiedNodes[nodeIndex];
        int index = parentNode.getIndex(currentNode) + 1;
        treeModel.insertNodeInto(copiedNode, parentNode, index);
    }
    instance.getMainFrame().repaint();
}