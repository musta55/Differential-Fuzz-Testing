private String getJndiKey(String key) {
    if (key.startsWith("java:comp/env")) {
        return key;
    }
    return BASE_NAME + key;
}