protected RequestTuple(byte[] buffer, int offset, int length) {
    super(buffer, offset, length);
    if (buffer == null || offset < 0 || length < 0 || offset + length > buffer.length) {
        throw new IllegalArgumentException("Invalid buffer, offset, or length");
    }
    parse();
    if (!isValid()) {
        logger.error("Invalid Request Tuple of type {} received!", getType());
    }
}