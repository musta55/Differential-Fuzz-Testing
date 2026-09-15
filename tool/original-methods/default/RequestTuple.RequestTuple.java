protected RequestTuple(byte[] buffer, int offset, int length) {
    super(buffer, offset, length);
    parse();
    if (!isValid()) {
        logger.error("Invalid Request Tuple of type {} received!", getType());
    }
}