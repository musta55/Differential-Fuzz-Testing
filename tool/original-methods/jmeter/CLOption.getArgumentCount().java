/**
 * Get number of arguments.
 *
 * @return the number of arguments
 */
public final int getArgumentCount() {
    if (null == this.arguments) {
        return 0;
    } else {
        return this.arguments.length;
    }
}