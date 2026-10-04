/**
 * Converts the specified map to a json-like object string.
 *
 * @param map
 *            the map
 * @return the string
 */
public static String toJsonObject(Map<String, String> map) {
    String result = "{";
    if (map != null) {
        String[] array = new String[map.size()];
        int index = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            array[index] = '"' + entry.getKey() + "\": " + entry.getValue();
            index++;
        }
        result += StringUtils.join(array, ", ");
    }
    return result + "}";
}