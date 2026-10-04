/**
 * Mutator of Argument property.
 *
 * @param argument
 *            the argument
 */
public final void addArgument(final String argument) {
    if (this.arguments == null) {
        this.arguments = new ArrayList<>();
    }
    this.arguments.add(argument);
}