/**
 * This is used by an application to evaluate a string containing
 * some expression.
 */
@Override
public Object eval(String source, int lineNo, int columnNo, Object oscript) throws BSFException {
    String scriptText = oscript.toString();
    Object retval = null;
    Context cx = Context.enter();
    try {
        retval = cx.evaluateString(global, scriptText, source, lineNo, null);
        if (retval instanceof NativeJavaObject) {
            retval = ((NativeJavaObject) retval).unwrap();
        }
    } catch (Throwable t) {
        handleError(t);
    } finally {
        Context.exit();
    }
    return retval;
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

