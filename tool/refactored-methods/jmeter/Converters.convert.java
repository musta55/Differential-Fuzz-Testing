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
    if (clazz.isAssignableFrom(String.class)) {
        // OK because checked above
        @SuppressWarnings("unchecked")
        T temp = (T) value;
        return temp;
    }
    StringConverter<T> converter = getConverter(clazz);
    if (converter == null) {
        throw new ConvertException(value, clazz.getName());
    }
    return converter.convert(value);
}
// ---- helper method(s) introduced by the refactoring ----
private static void registerCharacterConverter() {
    StringConverter<Character> characterConverter = value -> {
        try {
            return value.charAt(0);
        } catch (NumberFormatException ex) {
            throw new ConvertException(value, Character.class.getName(), ex);
        }
    };
    CONVERTER_MAP.put(Character.class, characterConverter);
    CONVERTER_MAP.put(char.class, characterConverter);
}

private static void registerDoubleConverter() {
    StringConverter<Double> doubleConverter = value -> {
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new ConvertException(value, Double.class.getName(), ex);
        }
    };
    CONVERTER_MAP.put(Double.class, doubleConverter);
    CONVERTER_MAP.put(double.class, doubleConverter);
}

private static void registerFloatConverter() {
    StringConverter<Float> floatConverter = value -> {
        try {
            return Float.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new ConvertException(value, Float.class.getName(), ex);
        }
    };
    CONVERTER_MAP.put(Float.class, floatConverter);
    CONVERTER_MAP.put(float.class, floatConverter);
}

private static void registerIntegerConverter() {
    StringConverter<Integer> integerConverter = value -> {
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            throw new ConvertException(value, Integer.class.getName(), ex);
        }
    };
    CONVERTER_MAP.put(Integer.class, integerConverter);
    CONVERTER_MAP.put(int.class, integerConverter);
}

private static void registerLongConverter() {
    StringConverter<Long> longConverter = value -> {
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            throw new ConvertException(value, Long.class.getName(), ex);
        }
    };
    CONVERTER_MAP.put(Long.class, longConverter);
    CONVERTER_MAP.put(long.class, longConverter);
}

private static void registerBooleanConverter() {
    StringConverter<Boolean> booleanConverter = Boolean::valueOf;
    CONVERTER_MAP.put(Boolean.class, booleanConverter);
    CONVERTER_MAP.put(boolean.class, booleanConverter);
}

private static void registerFileConverter() {
    CONVERTER_MAP.put(File.class, (StringConverter<File>) File::new);
}

