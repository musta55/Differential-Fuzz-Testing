private Dimension layoutSize(Container parent, boolean minimum) {
    Dimension dim = new Dimension(0, 0);
    Dimension d;
    synchronized (parent.getTreeLock()) {
        int n = parent.getComponentCount();
        for (int i = 0; i < n; i++) {
            Component c = parent.getComponent(i);
            if (c.isVisible()) {
                d = minimum ? c.getMinimumSize() : c.getPreferredSize();
                dim.width = Math.max(dim.width, d.width);
                dim.height += d.height;
                if (i > 0) {
                    dim.height += vgap;
                }
            }
        }
    }
    Insets insets = parent.getInsets();
    dim.width += insets.left + insets.right;
    dim.height += insets.top + insets.bottom + vgap + vgap;
    return dim;
}