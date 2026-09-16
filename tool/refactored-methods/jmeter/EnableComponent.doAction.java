/**
 * @see org.apache.jmeter.gui.action.Command#doAction(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    JMeterTreeNode[] nodes = GuiPackage.getInstance().getTreeListener().getSelectedNodes();
    String actionCommand = e.getActionCommand();
    if (actionCommand.equals(ActionNames.ENABLE)) {
        log.debug("enabling currently selected gui objects");
        enableComponents(nodes, true);
    } else if (actionCommand.equals(ActionNames.DISABLE)) {
        log.debug("disabling currently selected gui objects");
        enableComponents(nodes, false);
    } else if (actionCommand.equals(ActionNames.TOGGLE)) {
        log.debug("toggling currently selected gui objects");
        toggleComponents(nodes);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void setComponentEnabled(JMeterTreeNode node, boolean enable, GuiPackage pack) {
    node.setEnabled(enable);
    pack.getGui(node.getTestElement()).setEnabled(enable);
}

