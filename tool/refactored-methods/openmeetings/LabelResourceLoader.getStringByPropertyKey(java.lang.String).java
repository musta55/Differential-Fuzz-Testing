private static String getStringByPropertyKey(String key) {
    String labelId = STRING_LABEL_MAPPING.get(key);
    return labelId != null ? Application.getString(labelId) : null;
}