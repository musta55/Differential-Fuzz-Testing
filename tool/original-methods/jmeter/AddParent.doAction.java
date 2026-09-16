@Override
public void doAction(ActionEvent e) {
    String name = ((Component) e.getSource()).getName();
    GuiPackage guiPackage = GuiPackage.getInstance();
    try {
        guiPackage.updateCurrentNode();
        TestElement controller = guiPackage.createTestElement(name);
        addParentToTree(controller);
    } catch (Exception err) {
        log.error("Exception while adding a TestElement.", err);
    }
}