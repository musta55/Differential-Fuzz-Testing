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
    Context cx;
    try {
        cx = Context.enter();
        // REMIND: convert arg list Vectors here?
        Object fun = global.get(method, global);
        // NOTE: Source and line arguments are nonsense in a call().
        //       Any way to make these arguments *sensible?
        if (fun == Scriptable.NOT_FOUND) {
            throw new EvaluatorException("function " + method + " not found.", "none", 0);
        }
        cx.setOptimizationLevel(-1);
        cx.setGeneratingDebug(false);
        cx.setGeneratingSource(false);
        cx.setOptimizationLevel(0);
        cx.setDebugger(null, null);
        retval = ((Function) fun).call(cx, global, global, args);
        if (retval instanceof Wrapper) {
            retval = ((Wrapper) retval).unwrap();
        }
    } catch (Throwable t) {
        //NOSONAR We handle correctly Error case in function
        handleError(t);
    } finally {
        Context.exit();
    }
    return retval;
}