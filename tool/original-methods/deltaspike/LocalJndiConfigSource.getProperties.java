@Override
public Map<String, String> getProperties() {
    Map<String, String> result = new HashMap<String, String>();
    result.putAll(JndiUtils.list(BASE_NAME, String.class));
    result.putAll(JndiUtils.list("java:comp/env", String.class));
    return result;
}