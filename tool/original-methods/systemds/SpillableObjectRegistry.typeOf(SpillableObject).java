private static byte typeOf(SpillableObject obj) throws IOException {
    if (obj instanceof IndexedMatrixValue)
        return INDEXED_MATRIX_VALUE;
    if (obj instanceof PackedBlock)
        return PACKED_BLOCK;
    throw new IOException("Unsupported spillable object type: " + obj.getClass().getName());
}