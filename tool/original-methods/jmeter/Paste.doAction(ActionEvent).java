/**
 * @see Command#doAction(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    JMeterTreeNode[] draggedNodes = Copy.getCopiedNodes();
    if (draggedNodes == null) {
        Toolkit.getDefaultToolkit().beep();
        return;
    }
    JMeterTreeListener treeListener = GuiPackage.getInstance().getTreeListener();
    JMeterTreeNode currentNode = treeListener.getCurrentNode();
    if (MenuFactory.canAddTo(currentNode, draggedNodes)) {
        Arrays.stream(draggedNodes).filter(Objects::nonNull).forEach(draggedNode -> addNode(currentNode, draggedNode));
    } else {
        Toolkit.getDefaultToolkit().beep();
    }
    GuiPackage.getInstance().getMainFrame().repaint();
}