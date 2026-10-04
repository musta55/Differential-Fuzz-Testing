/**
 * Use this static method if you want to center and set its position
 * compared to the size of the current users screen size. Valid percent is
 * between +-(0-100) minus is treated as plus, bigger than 100 is always set
 * to 100.
 *
 * @param component
 *            the component you want to center and set size on
 * @param percentOfScreen
 *            the percent of the current screen size you want the component
 *            to be
 */
public static void centerComponentInWindow(Component component, int percentOfScreen) {
    int validPercentOfScreen = Math.min(Math.abs(percentOfScreen), 100);
    double percent = validPercentOfScreen / 100.0;
    Rectangle bounds = getScreenBounds();
    setSize(component, bounds, percent);
    centerComponentInWindow(component);
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

