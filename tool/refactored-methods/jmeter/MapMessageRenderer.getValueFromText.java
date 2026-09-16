@Override
public Map<String, Object> getValueFromText(String text) {
    Map<String, Object> m = new HashMap<>();
    String[] lines = text.split("\n");
    for (String line : lines) {
        String[] parts = line.split(",", 3);
        if (parts.length != 3) {
            throw new IllegalArgumentException("line must have 3 parts: " + line);
        }
        String name = parts[0];
        String type = parts[1];
        if (!type.contains(".")) {
            // Allow shorthand names
            type = "java.lang." + type;
        }
        String value = parts[2];
        Object obj = convertToObject(type, value);
        m.put(name, obj);
    }
    return m;
}
// ---- helper method(s) introduced by the refactoring ----
private static Object convertToObject(String type, String value) {
    if (type.equals("java.lang.String")) {
        return value;
    } else {
        try {
            Class<?> clazz = Class.forName(type);
            Method method = clazz.getMethod("valueOf", String.class);
            return method.invoke(clazz, value);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(String.format("Can't convert %s to object", value), e);
        }
    }
}

