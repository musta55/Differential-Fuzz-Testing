@Override
public void run() {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    try {
        //$NON-NLS-1$
        Class<?> interpreter = loader.loadClass("bsh.Interpreter");
        Object instance = interpreter.getDeclaredConstructor().newInstance();
        Class<String> string = String.class;
        Class<Object> object = Object.class;
        //$NON-NLS-1$
        Method eval = interpreter.getMethod("eval", string);
        //$NON-NLS-1$
        Method setObj = interpreter.getMethod("set", string, object);
        //$NON-NLS-1$
        Method setInt = interpreter.getMethod("set", string, int.class);
        //$NON-NLS-1$
        Method source = interpreter.getMethod("source", string);
        //$NON-NLS-1$
        setObj.invoke(instance, "t", this);
        //$NON-NLS-1$
        setInt.invoke(instance, "portnum", serverport);
        if (serverfile.length() > 0) {
            try {
                source.invoke(instance, serverfile);
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
        eval.invoke(instance, "setAccessibility(true);");
        //$NON-NLS-1$
        eval.invoke(instance, "server(portnum);");
    } catch (ClassNotFoundException e) {
        log.error("Beanshell Interpreter not found");
    } catch (Exception e) {
        log.error("Problem starting BeanShell server", e);
    }
}