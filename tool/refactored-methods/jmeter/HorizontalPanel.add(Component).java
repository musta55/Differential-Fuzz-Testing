/**
 * {@inheritDoc}
 */
@Override
public Component add(Component c) {
    // This won't work right if we remove components. But we don't, so I'm
    // not going to worry about it right now.
    if (hgap > 0 && subPanel.getComponentCount() > 0) {
        subPanel.add(createHorizontalStrut());
    }
    if (c instanceof JComponent) {
        ((JComponent) c).setAlignmentY(verticalAlign);
    }
    return subPanel.add(c);
}
// ---- helper method(s) introduced by the refactoring ----
private Component createHorizontalStrut() {
    return Box.createHorizontalStrut(hgap);
}

