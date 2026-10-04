@Override
public void declareBean(BSFDeclaredBean bean) throws BSFException {
    Object value = bean.bean;
    if (value instanceof Number || value == null || value instanceof String || value instanceof Boolean) {
        global.put(bean.name, global, value);
    } else {
        Scriptable wrapped = Context.toObject(value, global);
        global.put(bean.name, global, wrapped);
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

