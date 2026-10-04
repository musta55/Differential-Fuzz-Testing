public static SpillableObject read(DataInput in) throws IOException {
    byte type = in.readByte();
    SpillableObject obj = switch(type) {
        case INDEXED_MATRIX_VALUE ->
            new IndexedMatrixValue();
        case PACKED_BLOCK ->
            new PackedBlock();
        default ->
            throw new IOException("Unknown spillable object type: " + type);
    };
    obj.read(in);
    return obj;
}