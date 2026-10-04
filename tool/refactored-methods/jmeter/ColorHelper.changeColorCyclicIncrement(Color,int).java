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
    int[] components = { col.getRed(), col.getGreen(), col.getBlue() };
    int minIndex = findIndexOfMinimumComponent(components);
    for (int i = 0; i < components.length; i++) {
        if (i == minIndex || (components[i] == components[minIndex] && i > minIndex)) {
            components[i] = (components[i] + inc) % 256;
            break;
        }
    }
    return new Color(components[0], components[1], components[2]);
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Finds the index of the minimum component in the array.
 *
 * @param components the array of color components
 * @return the index of the minimum component
 */
private static int findIndexOfMinimumComponent(int[] components) {
    int minIndex = 0;
    for (int i = 1; i < components.length; i++) {
        if (components[i] < components[minIndex]) {
            minIndex = i;
        }
    }
    return minIndex;
}

