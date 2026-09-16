/**
 * {@inheritDoc}
 */
@Override
public Component add(Component c) {
    addVerticalStrutIfNecessary();
    setAlignmentForJComponent(c);
    return subPanel.add(c);
}
// ---- helper method(s) introduced by the refactoring ----
private void addVerticalStrutIfNecessary() {
    if (vgap > 0 && subPanel.getComponentCount() > 0) {
        subPanel.add(Box.createVerticalStrut(vgap));
    }
}

private void setAlignmentForJComponent(Component c) {
    if (c instanceof JComponent) {
        ((JComponent) c).setAlignmentX(horizontalAlign);
    }
}

