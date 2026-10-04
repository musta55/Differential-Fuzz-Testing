/**
 * Call a class constructor with an integer parameter
 * @param className name of the class to be constructed
 * @param parameter the value to be used in the constructor
 * @return an instance of the class
 * @throws JMeterException if class cannot be created
 */
public static Object construct(String className, int parameter) throws JMeterException {
    Object instance = null;
    try {
        Class<?> clazz = ClassUtils.getClass(className);
        Constructor<?> constructor = clazz.getConstructor(Integer.TYPE);
        instance = constructor.newInstance(parameter);
    } catch (ClassNotFoundException | InvocationTargetException | IllegalArgumentException | NoSuchMethodException | SecurityException | IllegalAccessException | InstantiationException e) {
        throw new JMeterException(e);
    }
    return instance;
}