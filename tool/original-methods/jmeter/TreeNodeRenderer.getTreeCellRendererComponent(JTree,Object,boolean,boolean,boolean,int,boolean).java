@Override
public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean focus) {
    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, focus);
    Object obj = ((DefaultMutableTreeNode) value).getUserObject();
    if (obj instanceof SampleResult) {
        if (!((SampleResult) obj).isSuccessful()) {
            this.setForeground(UIManager.getColor(JMeterUIDefaults.LABEL_ERROR_FOREGROUND));
            this.setIcon(imageFailure);
        } else {
            this.setIcon(imageSuccess);
        }
    }
    return this;
}