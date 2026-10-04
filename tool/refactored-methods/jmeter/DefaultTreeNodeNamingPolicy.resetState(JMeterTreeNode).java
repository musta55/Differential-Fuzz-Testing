/**
 * @see org.apache.jmeter.gui.action.TreeNodeNamingPolicy#resetState(org.apache.jmeter.gui.tree.JMeterTreeNode)
 */
@Override
public void resetState(JMeterTreeNode rootNode) {
    int numberOfChildren = rootNode.getChildCount();
    this.index = 0;
    this.formatter = new DecimalFormat(String.join("", java.util.Collections.nCopies(String.valueOf(numberOfChildren).length(), "0")));
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean shouldRename(JMeterTreeNode childNode) {
    Object userObject = childNode.getUserObject();
    return userObject instanceof TransactionController || userObject instanceof Sampler;
}

