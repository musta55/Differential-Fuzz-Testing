/**
 * Finds a inner ResultData matching the specified data name in a ResultData
 * tree. Supports only MapResultData walking.
 *
 * @param data
 *            the name of the data containing the value
 * @param root
 *            the root of the tree
 * @return the ResultData matching the data name
 */
protected static ResultData findData(String data, ResultData root) {
    String[] pathItems = StringUtils.split(data, '.');
    if (pathItems == null || !(root instanceof MapResultData)) {
        return null;
    }
    MapResultData map = (MapResultData) root;
    for (String pathItem : pathItems) {
        ResultData current = map.getResult(pathItem);
        if (current instanceof MapResultData) {
            map = (MapResultData) current;
        } else {
            return current;
        }
    }
    return null;
}
// ---- helper method(s) introduced by the refactoring ----
private static <T> T castObject(Class<T> clazz, Object object) {
    return clazz.cast(object);
}

