public static String getName(Method method, int parameterIndex) {
    if (!isParameterSupported() || method == null) {
        return null;
    }
    try {
        Object[] parameters = (Object[]) getParametersMethod.invoke(method);
        return (String) getNameMethod.invoke(parameters[parameterIndex]);
    } catch (IllegalAccessException e) {
    } catch (InvocationTargetException e) {
    }
    return null;
}