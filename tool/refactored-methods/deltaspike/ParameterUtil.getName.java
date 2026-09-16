public static String getName(Method method, int parameterIndex) {
    if (!isParameterSupported() || method == null) {
        return null;
    }
    try {
        Object[] parameters = (Object[]) getParametersMethod.invoke(method);
        return (String) getNameMethod.invoke(parameters[parameterIndex]);
    } catch (IllegalAccessException e) {
        // Log the IllegalAccessException
        e.printStackTrace();
    } catch (InvocationTargetException e) {
        // Log the InvocationTargetException and its cause
        e.printStackTrace();
        if (e.getCause() != null) {
            e.getCause().printStackTrace();
        }
    }
    return null;
}