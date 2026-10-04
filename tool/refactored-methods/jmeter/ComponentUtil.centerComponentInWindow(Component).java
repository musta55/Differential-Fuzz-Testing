/**
 * Use this static method if you want to center a component in Window.
 *
 * @param component
 *            the component you want to center in window
 */
public static void centerComponentInWindow(Component component) {
    Rectangle bounds = getScreenBounds();
    setLocation(component, bounds);
    component.validate();
    component.repaint();
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

