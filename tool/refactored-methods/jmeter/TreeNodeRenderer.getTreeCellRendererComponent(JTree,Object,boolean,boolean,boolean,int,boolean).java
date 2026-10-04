@Override
public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean focus) {
    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, focus);
    Object obj = ((DefaultMutableTreeNode) value).getUserObject();
    if (obj instanceof SampleResult) {
        SampleResult result = (SampleResult) obj;
        if (!result.isSuccessful()) {
            setFailureIconAndColor();
        } else {
            setSuccessIcon();
        }
    }
    return this;
}
// ---- helper method(s) introduced by the refactoring ----
private void setFailureIconAndColor() {
    this.setForeground(UIManager.getColor(JMeterUIDefaults.LABEL_ERROR_FOREGROUND));
    this.setIcon(imageFailure);
}

private void setSuccessIcon() {
    this.setIcon(imageSuccess);
}

