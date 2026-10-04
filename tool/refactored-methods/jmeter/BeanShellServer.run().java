@Override
public void run() {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    try {
        //$NON-NLS-1$
        Class<?> interpreter = loader.loadClass("bsh.Interpreter");
        interpreterInstance = interpreter.getDeclaredConstructor().newInstance();
        Class<String> string = String.class;
        Class<Object> object = Object.class;
        //$NON-NLS-1$
        evalMethod = interpreter.getMethod("eval", string);
        //$NON-NLS-1$
        setObjMethod = interpreter.getMethod("set", string, object);
        //$NON-NLS-1$
        setIntMethod = interpreter.getMethod("set", string, int.class);
        //$NON-NLS-1$
        sourceMethod = interpreter.getMethod("source", string);
        setupInterpreter();
        startServer();
    } catch (ClassNotFoundException e) {
        log.error("Beanshell Interpreter not found");
    } catch (Exception e) {
        log.error("Problem starting BeanShell server", e);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void setupInterpreter() throws IllegalAccessException, InvocationTargetException {
    //$NON-NLS-1$
    setObjMethod.invoke(interpreterInstance, "t", this);
    //$NON-NLS-1$
    setIntMethod.invoke(interpreterInstance, "portnum", serverport);
    if (serverfile.length() > 0) {
        try {
            sourceMethod.invoke(interpreterInstance, serverfile);
        } catch (InvocationTargetException ite) {
            Throwable cause = ite.getCause();
            if (log.isWarnEnabled()) {
                log.warn("Could not source, {}. {}", serverfile, (cause != null) ? cause.toString() : ite.toString());
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
        }
    }
    //$NON-NLS-1$
    evalMethod.invoke(interpreterInstance, "setAccessibility(true);");
}

private void startServer() throws IllegalAccessException, InvocationTargetException {
    //$NON-NLS-1$
    evalMethod.invoke(interpreterInstance, "server(portnum);");
}

