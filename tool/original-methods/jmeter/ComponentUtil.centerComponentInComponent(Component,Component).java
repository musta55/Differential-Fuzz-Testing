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
    toBeCentered.setLocation(parent.getX() + (parent.getWidth() - toBeCentered.getWidth()) / 2, parent.getY() + (parent.getHeight() - toBeCentered.getHeight()) / 2);
    toBeCentered.validate();
    toBeCentered.repaint();
}