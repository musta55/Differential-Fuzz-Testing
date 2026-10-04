public static QDictionary create(byte[] values, double scale, int nCol, boolean check) {
    if (scale == 0)
        return null;
    if (check) {
        boolean containsOnlyZero = true;
        for (int i = 0; i < values.length && containsOnlyZero; i++) {
            if (values[i] != 0)
                containsOnlyZero = false;
        }
        if (containsOnlyZero)
            return null;
    }
    return new QDictionary(values, scale, nCol);
}