/**
 * Lays out the container.
 */
@Override
public void layoutContainer(Container parent) {
    Insets insets = parent.getInsets();
    synchronized (parent.getTreeLock()) {
        int n = parent.getComponentCount();
        Dimension pd = parent.getSize();
        int y = 0;
        // work out the total size
        for (int i = 0; i < n; i++) {
            Component c = parent.getComponent(i);
            Dimension d = c.getPreferredSize();
            y += d.height + vgap;
        }
        // otherwise there's a vgap too many
        y -= vgap;
        // Work out the anchor paint
        if (anchor == TOP) {
            y = insets.top;
        } else if (anchor == CENTER) {
            y = (pd.height - y) / 2;
        } else {
            y = pd.height - y - insets.bottom;
        }
        // do layout
        for (int i = 0; i < n; i++) {
            Component c = parent.getComponent(i);
            Dimension d = c.getPreferredSize();
            int x = insets.left;
            int wid = d.width;
            if (alignment == CENTER) {
                x = (pd.width - d.width) / 2;
            } else if (alignment == RIGHT) {
                x = pd.width - d.width - insets.right;
            } else if (alignment == BOTH) {
                wid = pd.width - insets.left - insets.right;
            }
            c.setBounds(x, y, wid, d.height);
            y += d.height + vgap;
        }
    }
}