public static QDictionary create(byte[] values, double scale, int nCol, boolean check) {
    if (scale == 0 || (check && containsOnlyZero(values)))
        return null;
    return new QDictionary(values, scale, nCol);
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean containsOnlyZero(byte[] values) {
    for (int i = 0; i < values.length; i++) {
        if (values[i] != 0)
            return false;
    }
    return true;
}

