/**
 * Use this static method if you want to center a component over another
 * component.
 *
 * @param parent
 *            the component you want to use to place it on
 * @param toBeCentered
 *            the component you want to center
 */
public static void centerComponentInComponent(Component parent, Component toBeCentered) {
    setLocation(toBeCentered, parent);
    toBeCentered.validate();
    toBeCentered.repaint();
}
// ---- helper method(s) introduced by the refactoring ----
private static Rectangle getScreenBounds() {
    return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
}

private static void setSize(Component component, Rectangle bounds, double percent) {
    component.setSize((int) (bounds.getWidth() * percent), (int) (bounds.getHeight() * percent));
}

private static void setLocation(Component component, Rectangle bounds) {
    component.setLocation((int) ((bounds.getWidth() - component.getWidth()) / 2), (int) ((bounds.getHeight() - component.getHeight()) / 2));
}

private static void setLocation(Component toBeCentered, Component parent) {
    toBeCentered.setLocation(parent.getX() + (parent.getWidth() - toBeCentered.getWidth()) / 2, parent.getY() + (parent.getHeight() - toBeCentered.getHeight()) / 2);
}

