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
    double percent = validPercentOfScreen / 100.d;
    Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
    component.setSize((int) (bounds.getWidth() * percent), (int) (bounds.getHeight() * percent));
    centerComponentInWindow(component);
}