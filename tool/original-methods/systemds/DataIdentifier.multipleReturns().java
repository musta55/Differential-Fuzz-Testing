/**
 * Method to specify if an expression returns multiple outputs.
 * This method must be overridden by all child classes.
 *
 * @return true if expression returns multiple outputs
 */
public boolean multipleReturns() {
    throw new LanguageException("multipleReturns() must be overridden in the subclass.");
}