@Override
public void readFields(DataInput arg0) throws IOException {
    int len = arg0.readInt();
    byte[] bytes = new byte[len];
    arg0.readFully(bytes);
    try {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        @SuppressWarnings("unchecked")
        Map<String, Object> properties = (Map<String, Object>) ois.readObject();
        setFieldsFromProperties(properties);
        ois.close();
    } catch (Exception e) {
        final Path path = Files.createTempFile("apex-rpc-raw-dump-", ".ser");
        logger.error("Failed to de-serialize {}. Writing raw data to {}.", this.getClass().getName(), path, e);
        Files.write(path, bytes);
        throw new IOException(e);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void setFieldsFromProperties(Map<String, Object> properties) throws IllegalAccessException {
    Field[] fields = this.getClass().getFields();
    AccessibleObject.setAccessible(fields, true);
    for (Field field : fields) {
        String fieldName = field.getName();
        if (properties.containsKey(fieldName)) {
            field.set(this, properties.get(fieldName));
        }
    }
}

private Map<String, Object> getPropertiesFromFields() throws IllegalAccessException {
    Map<String, Object> properties = new HashMap<>();
    Field[] fields = this.getClass().getFields();
    AccessibleObject.setAccessible(fields, true);
    for (Field field : fields) {
        if (!Modifier.isStatic(field.getModifiers())) {
            String fieldName = field.getName();
            Object fieldValue = field.get(this);
            properties.put(fieldName, fieldValue);
        }
    }
    return properties;
}

