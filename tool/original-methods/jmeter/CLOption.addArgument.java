/**
 * Mutator of Argument property.
 *
 * @param argument
 *            the argument
 */
public final void addArgument(final String argument) {
    if (null == this.arguments) {
        this.arguments = new String[] { argument };
    } else {
        final String[] arguments = new String[this.arguments.length + 1];
        System.arraycopy(this.arguments, 0, arguments, 0, this.arguments.length);
        arguments[this.arguments.length] = argument;
        this.arguments = arguments;
    }
}