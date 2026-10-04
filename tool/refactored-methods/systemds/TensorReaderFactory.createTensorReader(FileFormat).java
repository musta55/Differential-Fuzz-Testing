public static TensorReader createTensorReader(FileFormat fmt) {
    TensorReader reader = READER_MAP.get(fmt);
    if (reader == null) {
        throw new DMLRuntimeException("Failed to create tensor reader for unknown format: " + fmt.toString());
    }
    return reader;
}