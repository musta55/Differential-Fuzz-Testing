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
    ResultData result = null;
    int count = pathItems.length;
    int index = 0;
    MapResultData map = (MapResultData) root;
    while (index < count && result == null) {
        ResultData current = map.getResult(pathItems[index]);
        if (index == count - 1) {
            result = current;
        } else {
            if (current instanceof MapResultData) {
                map = (MapResultData) current;
                index++;
            }
        }
    }
    return result;
}