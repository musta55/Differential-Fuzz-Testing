@Override
public void doAction(ActionEvent e) {
    String name = ((Component) e.getSource()).getName();
    GuiPackage guiPackage = GuiPackage.getInstance();
    try {
        guiPackage.updateCurrentNode();
        TestElement controller = guiPackage.createTestElement(name);
        addParentToTree(guiPackage, controller);
    } catch (Exception err) {
        log.error("Exception while adding a TestElement.", err);
    }
}
// ---- helper method(s) introduced by the refactoring ----
protected static void addParentToTree(GuiPackage guiPackage, TestElement newParent) {
    JMeterTreeNode newNode = createNewNode(guiPackage, newParent);
    JMeterTreeNode currentNode = getCurrentNode(guiPackage);
    JMeterTreeNode parentNode = getParentNode(currentNode);
    int index = getIndex(parentNode, currentNode);
    insertNodeIntoTree(guiPackage, newNode, parentNode, index);
    moveSelectedNodes(guiPackage, newNode);
}

private static JMeterTreeNode createNewNode(GuiPackage guiPackage, TestElement newParent) {
    return new JMeterTreeNode(newParent, guiPackage.getTreeModel());
}

private static JMeterTreeNode getCurrentNode(GuiPackage guiPackage) {
    return guiPackage.getTreeListener().getCurrentNode();
}

private static JMeterTreeNode getParentNode(JMeterTreeNode currentNode) {
    return (JMeterTreeNode) currentNode.getParent();
}

private static int getIndex(JMeterTreeNode parentNode, JMeterTreeNode currentNode) {
    return parentNode.getIndex(currentNode);
}

private static void insertNodeIntoTree(GuiPackage guiPackage, JMeterTreeNode newNode, JMeterTreeNode parentNode, int index) {
    guiPackage.getTreeModel().insertNodeInto(newNode, parentNode, index);
}

private static void moveSelectedNodes(GuiPackage guiPackage, JMeterTreeNode newNode) {
    JMeterTreeNode[] selectedNodes = guiPackage.getTreeListener().getSelectedNodes();
    for (JMeterTreeNode node : selectedNodes) {
        moveNode(guiPackage, node, newNode);
    }
}

