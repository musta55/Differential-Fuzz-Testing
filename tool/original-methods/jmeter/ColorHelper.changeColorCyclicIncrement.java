/**
 * Given the <code>Color</code>, get the red, green and blue components.
 * Increment the lowest of the components by the indicated increment value.
 * If all the components are the same value increment in the order of red,
 * green and blue.
 *
 * @param col
 *            {@link Color} to start with
 * @param inc
 *            value to increment the color components
 * @return the color after change
 */
public static Color changeColorCyclicIncrement(Color col, int inc) {
    int red = col.getRed();
    int green = col.getGreen();
    int blue = col.getBlue();
    int temp1 = Math.min(red, green);
    int temp2 = Math.min(temp1, blue);
    // now temp2 has the lowest of the three components
    if (red == temp2) {
        red += inc;
        red %= 256;
    } else if (green == temp2) {
        green += inc;
        green %= 256;
    } else if (blue == temp2) {
        blue += inc;
        blue %= 256;
    }
    return new Color(red, green, blue);
}