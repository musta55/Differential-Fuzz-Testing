private static byte typeOf(SpillableObject obj) throws IOException {
    Byte type = TYPE_MAP.get(obj.getClass());
    if (type == null) {
        throw new IOException("Unsupported spillable object type: " + obj.getClass().getName());
    }
    return type;
}