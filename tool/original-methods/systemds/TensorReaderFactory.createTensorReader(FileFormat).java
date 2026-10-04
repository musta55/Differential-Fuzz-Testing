public static TensorReader createTensorReader(FileFormat fmt) {
    TensorReader reader;
    if (fmt == FileFormat.TEXT) {
        reader = new TensorReaderTextCell();
    } else if (fmt == FileFormat.BINARY) {
        reader = new TensorReaderBinaryBlock();
    } else {
        throw new DMLRuntimeException("Failed to create tensor reader for unknown format: " + fmt.toString());
    }
    return reader;
}