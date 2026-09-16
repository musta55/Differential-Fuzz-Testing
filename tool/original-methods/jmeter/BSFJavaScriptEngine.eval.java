/**
 * This is used by an application to evaluate a string containing
 * some expression.
 */
@Override
public Object eval(String source, int lineNo, int columnNo, Object oscript) throws BSFException {
    String scriptText = oscript.toString();
    Object retval = null;
    Context cx;
    try {
        cx = Context.enter();
        cx.setOptimizationLevel(-1);
        cx.setGeneratingDebug(false);
        cx.setGeneratingSource(false);
        cx.setOptimizationLevel(0);
        cx.setDebugger(null, null);
        retval = cx.evaluateString(global, scriptText, source, lineNo, null);
        if (retval instanceof NativeJavaObject) {
            retval = ((NativeJavaObject) retval).unwrap();
        }
    } catch (Throwable t) {
        // NOSONAR We handle correctly Error case in function, includes JavaScriptException, rethrows Errors
        handleError(t);
    } finally {
        Context.exit();
    }
    return retval;
}