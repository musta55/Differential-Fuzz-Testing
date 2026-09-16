private String getJndiKey(String key) {
    return key.startsWith("java:comp/env") ? key : BASE_NAME + key;
}