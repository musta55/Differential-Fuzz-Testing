@Override
public void doAction(ActionEvent e) {
    String name = ((Component) e.getSource()).getName();
    GuiPackage guiPackage = GuiPackage.getInstance();
    JMeterTreeNode currentNode = guiPackage.getTreeListener().getCurrentNode();
    if (!(currentNode.getUserObject() instanceof Controller)) {
        Toolkit.getDefaultToolkit().beep();
        return;
    }
    try {
        guiPackage.updateCurrentNode();
        TestElement controller = guiPackage.createTestElement(name);
        performChangeParent(controller, guiPackage, currentNode);
    } catch (Exception err) {
        Toolkit.getDefaultToolkit().beep();
        log.error("Failed to change parent", err);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void performChangeParent(TestElement newParent, GuiPackage guiPackage, JMeterTreeNode currentNode) {
    updateParentName(newParent, guiPackage, currentNode);
    insertNewParentAndReattachChildren(newParent, guiPackage, currentNode);
    selectNewNodeInTree(newParent, guiPackage);
}

private static void updateParentName(TestElement newParent, GuiPackage guiPackage, JMeterTreeNode currentNode) {
    Controller currentController = (Controller) currentNode.getUserObject();
    JMeterGUIComponent currentGui = guiPackage.getCurrentGui();
    String defaultName = JMeterUtils.getResString(currentGui.getLabelResource());
    if (StringUtils.isNotBlank(currentController.getName()) && !currentController.getName().equals(defaultName)) {
        newParent.setName(currentController.getName());
    }
}

private static void insertNewParentAndReattachChildren(TestElement newParent, GuiPackage guiPackage, JMeterTreeNode currentNode) {
    JMeterTreeModel treeModel = guiPackage.getTreeModel();
    JMeterTreeNode newNode = new JMeterTreeNode(newParent, treeModel);
    JMeterTreeNode parentNode = (JMeterTreeNode) currentNode.getParent();
    int index = parentNode.getIndex(currentNode);
    treeModel.insertNodeInto(newNode, parentNode, index);
    treeModel.removeNodeFromParent(currentNode);
    reattachChildren(treeModel, currentNode, newNode);
}

private static void reattachChildren(JMeterTreeModel treeModel, JMeterTreeNode currentNode, JMeterTreeNode newNode) {
    int childCount = currentNode.getChildCount();
    for (int i = 0; i < childCount; i++) {
        JMeterTreeNode node = (JMeterTreeNode) currentNode.getChildAt(0);
        treeModel.removeNodeFromParent(node);
        treeModel.insertNodeInto(node, newNode, newNode.getChildCount());
    }
}

private static void selectNewNodeInTree(TestElement newParent, GuiPackage guiPackage) {
    JMeterTreeModel treeModel = guiPackage.getTreeModel();
    JMeterTreeNode newNode = findNodeForTestElement(treeModel, newParent);
    TreeNode[] nodes = treeModel.getPathToRoot(newNode);
    JTree tree = guiPackage.getTreeListener().getJTree();
    tree.setSelectionPath(new TreePath(nodes));
}

private static JMeterTreeNode findNodeForTestElement(JMeterTreeModel treeModel, TestElement testElement) {
    return treeModel.getNodeOf(testElement);
}

