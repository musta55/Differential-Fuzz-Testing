/**
 * @see org.apache.jmeter.gui.action.TreeNodeNamingPolicy#resetState(org.apache.jmeter.gui.tree.JMeterTreeNode)
 */
@Override
public void resetState(JMeterTreeNode rootNode) {
    int numberOfChildren = rootNode.getChildCount();
    this.index = 0;
    int numberOfDigits = String.valueOf(numberOfChildren).length();
    StringBuilder formatSB = new StringBuilder(numberOfDigits);
    for (int i = 0; i < numberOfDigits; i++) {
        formatSB.append("0");
    }
    this.formatter = new DecimalFormat(formatSB.toString());
}