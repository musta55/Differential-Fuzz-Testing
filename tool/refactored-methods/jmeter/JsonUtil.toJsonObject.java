/**
 * Converts the specified map to a json-like object string.
 *
 * @param map
 *            the map
 * @return the string
 */
public static String toJsonObject(Map<String, String> map) {
    StringBuilder result = new StringBuilder("{");
    if (map != null) {
        boolean firstEntry = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!firstEntry) {
                result.append(", ");
            }
            result.append('"').append(entry.getKey()).append("\": \"").append(entry.getValue()).append('"');
            firstEntry = false;
        }
    }
    return result.append("}").toString();
}