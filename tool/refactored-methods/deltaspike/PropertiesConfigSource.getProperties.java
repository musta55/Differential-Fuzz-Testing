@Override
public Map<String, String> getProperties() {
    Map<String, String> result = new HashMap<>(properties.size());
    for (String propertyName : properties.stringPropertyNames()) {
        result.put(propertyName, properties.getProperty(propertyName));
    }
    return result;
}