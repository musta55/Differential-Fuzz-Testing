public static TensorWriter createTensorWriter(FileFormat fmt) {
    TensorWriter writer = null;
    if (fmt == FileFormat.TEXT) {
        writer = new TensorWriterTextCell();
    } else if (fmt == FileFormat.BINARY) {
        writer = new TensorWriterBinaryBlock();
    } else {
        throw new DMLRuntimeException("Failed to create tensor writer for unknown format: " + fmt.toString());
    }
    return writer;
}