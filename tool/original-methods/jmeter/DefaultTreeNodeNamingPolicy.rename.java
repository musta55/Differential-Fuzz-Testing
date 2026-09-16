/**
 * @see org.apache.jmeter.gui.action.TreeNodeNamingPolicy#rename(org.apache.jmeter.gui.tree.JMeterTreeNode, org.apache.jmeter.gui.tree.JMeterTreeNode, int)
 */
@Override
public void rename(JMeterTreeNode parentNode, JMeterTreeNode childNode, int iterationIndex) {
    if (childNode.getUserObject() instanceof TransactionController || childNode.getUserObject() instanceof Sampler) {
        childNode.setName(parentNode.getName() + TRANSACTION_CHILDREN_SEPARATOR + formatter.format(index));
        index++;
    }
}