/**
 * Retrieve indexed argument to option if it takes arguments.
 *
 * @param index
 *            The argument index, from 0 to {@link #getArgumentCount()}-1.
 * @return the argument
 */
public final String getArgument(final int index) {
    if (this.arguments == null || index < 0 || index >= this.arguments.size()) {
        return null;
    } else {
        return this.arguments.get(index);
    }
}