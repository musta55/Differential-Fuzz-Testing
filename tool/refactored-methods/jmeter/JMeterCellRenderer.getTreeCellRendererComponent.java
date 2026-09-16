@Override
public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean p_hasFocus) {
    JMeterTreeNode node = (JMeterTreeNode) value;
    super.getTreeCellRendererComponent(tree, getNodeName(node), sel, expanded, leaf, row, p_hasFocus);
    setNodeIcon(node);
    setNodeEnabled(node);
    setNodeBorder(node);
    return this;
}
// ---- helper method(s) introduced by the refactoring ----
private static String getNodeName(JMeterTreeNode node) {
    return JOrphanUtils.isBlank(node.getName()) ? BLANK : node.getName();
}

private void setNodeIcon(JMeterTreeNode node) {
    ImageIcon ic = node.getIcon(node.isEnabled());
    if (ic != null) {
        setIcon(ic);
    } else if (!node.isEnabled()) {
        ic = node.getIcon();
        if (ic != null) {
            setDisabledIcon(ic);
        }
    }
}

private void setNodeEnabled(JMeterTreeNode node) {
    this.setEnabled(node.isEnabled());
}

private void setNodeBorder(JMeterTreeNode node) {
    if (node.isMarkedBySearch()) {
        setBorder(RED_BORDER);
    } else if (node.isChildrenMarkedBySearch()) {
        setBorder(BLUE_BORDER);
    } else {
        setBorder(null);
    }
}

