/**
 * Return an object from an extension.
 * @param object Object on which to make the call (ignored).
 * @param method The name of the method to call.
 * @param args an array of arguments to be
 * passed to the extension, which may be either
 * Vectors of Nodes, or Strings.
 */
@Override
public Object call(Object object, String method, Object[] args) throws BSFException {
    Object retval = null;
    Context cx = Context.enter();
    try {
        Object fun = global.get(method, global);
        if (fun == Scriptable.NOT_FOUND) {
            throw new EvaluatorException("function " + method + " not found.", "none", 0);
        }
        retval = ((Function) fun).call(cx, global, global, args);
        if (retval instanceof Wrapper) {
            retval = ((Wrapper) retval).unwrap();
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

