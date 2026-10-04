/**
 * @param t {@link Throwable}
 * @throws BSFException
 */
private static void handleError(Throwable t) throws BSFException {
    Throwable target = t;
    if (t instanceof WrappedException) {
        target = ((WrappedException) t).getWrappedException();
    }
    String message = null;
    if (target instanceof JavaScriptException) {
        message = target.getLocalizedMessage();
        // Is it an exception wrapped in a JavaScriptException?
        Object value = ((JavaScriptException) target).getValue();
        if (value instanceof Throwable) {
            // likely a wrapped exception from a LiveConnect call.
            // Display its stack trace as a diagnostic
            target = (Throwable) value;
        }
    } else if (target instanceof EvaluatorException || target instanceof SecurityException) {
        message = target.getLocalizedMessage();
    } else if (target instanceof RuntimeException) {
        message = "Internal Error: " + target.toString();
    } else if (target instanceof StackOverflowError) {
        message = "Stack Overflow";
    }
    if (message == null) {
        message = target.toString();
    }
    if (target instanceof Error && !(target instanceof StackOverflowError)) {
        // Re-throw Errors because we're supposed to let the JVM see it
        // Don't re-throw StackOverflows, because we know we've
        // corrected the situation by aborting the loop and
        // a long stacktrace would end up on the user's console
        throw (Error) target;
    } else {
        throw new BSFException(BSFException.REASON_OTHER_ERROR, "JavaScript Error: " + message, target);
    }
}