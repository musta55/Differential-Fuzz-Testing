/**
 * @see Command#doAction(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    GuiPackage guiPack = GuiPackage.getInstance();
    ActionRouter.getInstance().actionPerformed(new ActionEvent(e.getSource(), e.getID(), ActionNames.CHECK_CUT));
    JMeterTreeNode[] currentNodes = guiPack.getTreeListener().getSelectedNodes();
    currentNodes = Copy.keepOnlyAncestors(currentNodes);
    Copy.setCopiedNodes(currentNodes);
    removeNodesFromParent(guiPack, currentNodes);
    guiPack.getMainFrame().repaint();
}
// ---- helper method(s) introduced by the refactoring ----
private static void removeNodesFromParent(GuiPackage guiPack, JMeterTreeNode[] nodes) {
    for (JMeterTreeNode node : nodes) {
        guiPack.getTreeModel().removeNodeFromParent(node);
    }
}

