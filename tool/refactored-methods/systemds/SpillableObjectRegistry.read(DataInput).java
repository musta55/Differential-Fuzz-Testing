public static SpillableObject read(DataInput in) throws IOException {
    byte type = in.readByte();
    Class<? extends SpillableObject> clazz = REVERSE_TYPE_MAP.get(type);
    if (clazz == null) {
        throw new IOException("Unknown spillable object type: " + type);
    }
    SpillableObject obj;
    try {
        obj = clazz.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
        throw new IOException("Failed to instantiate spillable object of type: " + type, e);
    }
    obj.read(in);
    return obj;
}