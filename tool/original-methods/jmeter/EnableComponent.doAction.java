/**
 * @see org.apache.jmeter.gui.action.Command#doAction(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    JMeterTreeNode[] nodes = GuiPackage.getInstance().getTreeListener().getSelectedNodes();
    if (e.getActionCommand().equals(ActionNames.ENABLE)) {
        log.debug("enabling currently selected gui objects");
        enableComponents(nodes, true);
    } else if (e.getActionCommand().equals(ActionNames.DISABLE)) {
        log.debug("disabling currently selected gui objects");
        enableComponents(nodes, false);
    } else if (e.getActionCommand().equals(ActionNames.TOGGLE)) {
        log.debug("toggling currently selected gui objects");
        toggleComponents(nodes);
    }
}