public static TensorWriter createTensorWriter(FileFormat fmt) {
    TensorWriter writer = WRITER_MAP.get(fmt);
    if (writer == null) {
        throw new DMLRuntimeException("Failed to create tensor writer for unknown format: " + fmt.toString());
    }
    return writer;
}