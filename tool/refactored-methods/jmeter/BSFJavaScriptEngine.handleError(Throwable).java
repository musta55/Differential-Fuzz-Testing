/**
 * @param t {@link Throwable}
 * @throws BSFException
 */
private static void handleError(Throwable t) throws BSFException {
    Throwable target = unwrapException(t);
    String message = getMessage(target);
    if (target instanceof Error && !(target instanceof StackOverflowError)) {
        throw (Error) target;
    } else {
        throw new BSFException(BSFException.REASON_OTHER_ERROR, "JavaScript Error: " + message, target);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static Throwable unwrapException(Throwable t) {
    if (t instanceof WrappedException) {
        return ((WrappedException) t).getWrappedException();
    }
    return t;
}

private static String getMessage(Throwable target) {
    if (target instanceof JavaScriptException) {
        Object value = ((JavaScriptException) target).getValue();
        if (value instanceof Throwable) {
            return value.toString();
        }
    }
    if (target instanceof EvaluatorException || target instanceof SecurityException) {
        return target.getLocalizedMessage();
    }
    if (target instanceof RuntimeException) {
        return "Internal Error: " + target.toString();
    }
    if (target instanceof StackOverflowError) {
        return "Stack Overflow";
    }
    return target.toString();
}

