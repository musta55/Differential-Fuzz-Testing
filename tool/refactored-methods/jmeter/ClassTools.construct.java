/**
 * Call constructor for a class with optional parameters.
 *
 * @param className name of the class to be constructed
 * @param parameters optional parameters to be used in the constructor
 * @return an instance of the class
 * @throws JMeterException if class cannot be created
 */
public static Object construct(String className, Object... parameters) throws JMeterException {
    Object instance = null;
    try {
        Class<?> clazz = ClassUtils.getClass(className);
        Class<?>[] parameterTypes = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            parameterTypes[i] = parameters[i].getClass();
        }
        Constructor<?> constructor = clazz.getConstructor(parameterTypes);
        instance = constructor.newInstance(parameters);
    } catch (ClassNotFoundException | InvocationTargetException | IllegalArgumentException | NoSuchMethodException | SecurityException | IllegalAccessException | InstantiationException e) {
        throw new JMeterException(e);
    }
    return instance;
}