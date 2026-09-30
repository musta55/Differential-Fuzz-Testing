@Override
public void readFields(DataInput arg0) throws IOException {
    int len = arg0.readInt();
    byte[] bytes = new byte[len];
    arg0.readFully(bytes);
    try {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        @SuppressWarnings("unchecked")
        Map<String, Object> properties = (Map<String, Object>) ois.readObject();
        Field[] fields = this.getClass().getFields();
        AccessibleObject.setAccessible(fields, true);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            if (properties.containsKey(fieldName)) {
                field.set(this, properties.get(fieldName));
            }
        }
        ois.close();
    } catch (Exception e) {
        final Path path = Files.createTempFile("apex-rpc-raw-dump-", ".ser");
        logger.error("Failed to de-serialize {}. Writing raw data to {}.", this.getClass().getName(), path, e);
        Files.write(path, bytes);
        throw new IOException(e);
    }
}