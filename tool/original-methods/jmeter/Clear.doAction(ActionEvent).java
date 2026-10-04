@Override
public void doAction(ActionEvent e) {
    GuiPackage guiPackage = GuiPackage.getInstance();
    final String actionCommand = e.getActionCommand();
    if (actionCommand.equals(ActionNames.CLEAR)) {
        JMeterGUIComponent guiComp = guiPackage.getCurrentGui();
        if (guiComp instanceof Clearable) {
            ((Clearable) guiComp).clearData();
        }
    } else {
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
}