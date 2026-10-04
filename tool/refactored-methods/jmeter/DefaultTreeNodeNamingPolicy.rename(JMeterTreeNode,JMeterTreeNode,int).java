/**
 * @see org.apache.jmeter.gui.action.TreeNodeNamingPolicy#rename(org.apache.jmeter.gui.tree.JMeterTreeNode, org.apache.jmeter.gui.tree.JMeterTreeNode, int)
 */
@Override
public void rename(JMeterTreeNode parentNode, JMeterTreeNode childNode, int iterationIndex) {
    if (shouldRename(childNode)) {
        childNode.setName(parentNode.getName() + TRANSACTION_CHILDREN_SEPARATOR + formatter.format(index));
        index++;
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean shouldRename(JMeterTreeNode childNode) {
    Object userObject = childNode.getUserObject();
    return userObject instanceof TransactionController || userObject instanceof Sampler;
}

