/**
 * Converts the specified value to the destination type
 *
 * @param <T>   the target type
 * @param clazz the target class
 * @param value the value to convert
 * @return the converted value
 * @throws ConvertException when the conversion failed
 */
public static <T> T convert(Class<T> clazz, String value) throws ConvertException {
    T result;
    if (clazz.isAssignableFrom(String.class)) {
        // OK because checked above
        @SuppressWarnings("unchecked")
        T temp = (T) value;
        result = temp;
    } else {
        StringConverter<T> converter = Converters.getConverter(clazz);
        if (converter == null) {
            throw new ConvertException(value, clazz.getName());
        }
        result = converter.convert(value);
    }
    return result;
}