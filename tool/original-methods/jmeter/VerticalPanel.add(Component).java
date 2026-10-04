/**
 * {@inheritDoc}
 */
@Override
public Component add(Component c) {
    // This won't work right if we remove components. But we don't, so I'm
    // not going to worry about it right now.
    if (vgap > 0 && subPanel.getComponentCount() > 0) {
        subPanel.add(Box.createVerticalStrut(vgap));
    }
    if (c instanceof JComponent) {
        ((JComponent) c).setAlignmentX(horizontalAlign);
    }
    return subPanel.add(c);
}