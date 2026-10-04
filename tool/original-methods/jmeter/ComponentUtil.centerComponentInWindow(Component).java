/**
 * Use this static method if you want to center a component in Window.
 *
 * @param component
 *            the component you want to center in window
 */
public static void centerComponentInWindow(Component component) {
    Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
    component.setLocation((int) ((bounds.getWidth() - component.getWidth()) / 2), (int) ((bounds.getHeight() - component.getHeight()) / 2));
    component.validate();
    component.repaint();
}