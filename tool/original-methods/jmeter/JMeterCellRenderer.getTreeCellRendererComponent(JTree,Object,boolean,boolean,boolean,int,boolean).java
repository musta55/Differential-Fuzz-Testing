@Override
public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean p_hasFocus) {
    JMeterTreeNode node = (JMeterTreeNode) value;
    super.getTreeCellRendererComponent(tree, JOrphanUtils.isBlank(node.getName()) ? BLANK : node.getName(), sel, expanded, leaf, row, p_hasFocus);
    boolean enabled = node.isEnabled();
    ImageIcon ic = node.getIcon(enabled);
    if (ic != null) {
        if (enabled) {
            setIcon(ic);
        } else {
            setDisabledIcon(ic);
        }
    } else {
        if (// i.e. no disabled icon found
        !enabled) {
            // Must therefore set the enabled icon so there is at least some
            // icon
            ic = node.getIcon();
            if (ic != null) {
                setDisabledIcon(ic);
            }
        }
    }
    this.setEnabled(enabled);
    if (node.isMarkedBySearch()) {
        setBorder(RED_BORDER);
    } else if (node.isChildrenMarkedBySearch()) {
        setBorder(BLUE_BORDER);
    } else {
        setBorder(null);
    }
    return this;
}