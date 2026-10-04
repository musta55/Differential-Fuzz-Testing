private static void addNode(JMeterTreeNode parent, JMeterTreeNode node) {
    try {
        // Add this node
        JMeterTreeNode newNode = GuiPackage.getInstance().getTreeModel().addComponent(node.getTestElement(), parent);
        // Add all the child nodes of the node we are adding
        for (int i = 0; i < node.getChildCount(); i++) {
            addNode(newNode, (JMeterTreeNode) node.getChildAt(i));
        }
    } catch (IllegalUserActionException iuae) {
        // $NON-NLS-1$
        log.error("Illegal user action while adding a tree node.", iuae);
        JMeterUtils.reportErrorToUser(iuae.getMessage());
    }
}