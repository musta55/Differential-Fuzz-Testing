private Dimension layoutSize(Container parent, boolean minimum) {
    Dimension dim = new Dimension(0, 0);
    synchronized (parent.getTreeLock()) {
        for (Component c : parent.getComponents()) {
            if (c.isVisible()) {
                Dimension d = minimum ? c.getMinimumSize() : c.getPreferredSize();
                dim.width = Math.max(dim.width, d.width);
                dim.height += d.height + vgap;
            }
        }
        if (dim.height > vgap) {
            // subtract last vgap
            dim.height -= vgap;
        }
    }
    Insets insets = parent.getInsets();
    dim.width += insets.left + insets.right;
    dim.height += insets.top + insets.bottom;
    return dim;
}
// ---- helper method(s) introduced by the refactoring ----
private int calculateTotalHeight(Container parent) {
    int totalHeight = 0;
    for (int i = 0; i < parent.getComponentCount(); i++) {
        Component c = parent.getComponent(i);
        Dimension d = c.getPreferredSize();
        totalHeight += d.height + vgap;
    }
    // subtract last vgap
    return totalHeight - vgap;
}

private int calculateYPosition(Container parent, int totalHeight, Insets insets) {
    int yPosition = 0;
    if (anchor == TOP) {
        yPosition = insets.top;
    } else if (anchor == CENTER) {
        yPosition = (parent.getHeight() - totalHeight) / 2;
    } else {
        yPosition = parent.getHeight() - totalHeight - insets.bottom;
    }
    return yPosition;
}

private void performLayout(Container parent, int yPosition, Insets insets) {
    for (int i = 0; i < parent.getComponentCount(); i++) {
        Component c = parent.getComponent(i);
        Dimension d = c.getPreferredSize();
        int x = calculateXPosition(parent, d.width, insets);
        int width = calculateWidth(parent, d.width, insets);
        c.setBounds(x, yPosition, width, d.height);
        yPosition += d.height + vgap;
    }
}

private int calculateXPosition(Container parent, int componentWidth, Insets insets) {
    int x = insets.left;
    if (alignment == CENTER) {
        x = (parent.getWidth() - componentWidth) / 2;
    } else if (alignment == RIGHT) {
        x = parent.getWidth() - componentWidth - insets.right;
    }
    return x;
}

private int calculateWidth(Container parent, int componentWidth, Insets insets) {
    int width = componentWidth;
    if (alignment == BOTH) {
        width = parent.getWidth() - insets.left - insets.right;
    }
    return width;
}

