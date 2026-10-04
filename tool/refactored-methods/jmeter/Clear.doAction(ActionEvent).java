@Override
public void doAction(ActionEvent e) {
    GuiPackage guiPackage = GuiPackage.getInstance();
    final String actionCommand = e.getActionCommand();
    if (actionCommand.equals(ActionNames.CLEAR)) {
        clearCurrentGui(guiPackage);
    } else {
        clearAllGui(guiPackage);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void clearCurrentGui(GuiPackage guiPackage) {
    JMeterGUIComponent guiComp = guiPackage.getCurrentGui();
    if (guiComp instanceof Clearable) {
        ((Clearable) guiComp).clearData();
    }
}

private static void clearAllGui(GuiPackage guiPackage) {
    guiPackage.getMainFrame().clearData();
    for (JMeterTreeNode node : guiPackage.getTreeModel().getNodesOfType(Clearable.class)) {
        JMeterGUIComponent guiComp = guiPackage.getGui(node.getTestElement());
        if (guiComp instanceof Clearable) {
            Clearable item = (Clearable) guiComp;
            try {
                item.clearData();
            } catch (Exception ex) {
                log.error("Can't clear: {} {}", node, guiComp, ex);
            }
        }
    }
}

