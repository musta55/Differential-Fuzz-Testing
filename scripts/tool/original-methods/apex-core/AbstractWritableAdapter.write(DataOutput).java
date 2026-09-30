@Override
public void write(DataOutput arg0) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    try {
        Map<String, Object> properties = new HashMap<>();
        Field[] fields = this.getClass().getFields();
        AccessibleObject.setAccessible(fields, true);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (!Modifier.isStatic(field.getModifiers())) {
                String fieldName = field.getName();
                Object fieldValue = field.get(this);
                properties.put(fieldName, fieldValue);
            }
        }
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