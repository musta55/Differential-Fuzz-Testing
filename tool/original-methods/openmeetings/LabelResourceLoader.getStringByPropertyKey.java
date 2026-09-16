private static String getStringByPropertyKey(String key) {
    String labelId = STRING_LABEL_MAPPING.get(key);
    if (labelId == null) {
        return null;
    }
    return Application.getString(labelId);
}