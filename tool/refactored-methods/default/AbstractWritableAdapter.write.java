@Override
public void write(DataOutput arg0) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    try {
        Map<String, Object> properties = getPropertiesFromFields();
        oos.writeObject(properties);
    } catch (Exception e) {
        throw new IOException(e);
    }
    oos.flush();
    byte[] bytes = bos.toByteArray();
    arg0.writeInt(bytes.length);
    arg0.write(bytes);
    oos.close();
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

