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
    for (JMeterTreeNode currentNode : currentNodes) {
        guiPack.getTreeModel().removeNodeFromParent(currentNode);
    }
    guiPack.getMainFrame().repaint();
}