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
        changeParent(controller, guiPackage, currentNode);
    } catch (Exception err) {
        Toolkit.getDefaultToolkit().beep();
        log.error("Failed to change parent", err);
    }
}