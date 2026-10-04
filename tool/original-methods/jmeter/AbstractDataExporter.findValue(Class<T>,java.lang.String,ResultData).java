/**
 * Finds a value matching the specified data name in a ResultData tree.
 * Supports only MapResultData walking.
 *
 * @param clazz
 *            the type of the value
 * @param data
 *            the name of the data containing the value
 * @param root
 *            the root of the tree
 * @param <T>
 *            type of value to be found
 * @return the value matching the data name
 */
protected static <T> T findValue(Class<T> clazz, String data, ResultData root) {
    T value = null;
    ResultData result = findData(data, root);
    if (result instanceof ValueResultData) {
        ValueResultData valueResult = (ValueResultData) result;
        Object object = valueResult.getValue();
        if (object != null && clazz.isAssignableFrom(object.getClass())) {
            value = clazz.cast(object);
        }
    }
    return value;
}