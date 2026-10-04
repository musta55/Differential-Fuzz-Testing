public void toBuffer(H5BufferBuilder bb) {
    bb.writeByte(CLASS_AND_VERSION_MAGIC_NUMBER);
    bb.writeBytes(CLASS_BITS_MAGIC_NUMBER);
    bb.writeInt(SIZE_MAGIC_NUMBER);
    bb.writeShort(BIT_OFFSET_MAGIC_NUMBER);
    bb.writeShort(BIT_PRECISION_MAGIC_NUMBER);
    bb.writeByte(EXPONENT_LOCATION_MAGIC_NUMBER);
    bb.writeByte(EXPONENT_SIZE_MAGIC_NUMBER);
    bb.writeByte(MANTISSA_LOCATION_MAGIC_NUMBER);
    bb.writeByte(MANTISSA_SIZE_MAGIC_NUMBER);
    bb.writeInt(EXPONENT_BIAS_MAGIC_NUMBER);
    bb.writeInt(RESERVED_MAGIC_NUMBER);
}
// ---- helper method(s) introduced by the refactoring ----
private byte[] readBytes(ByteBuffer bb, int length) {
    byte[] bytes = new byte[length];
    bb.get(bytes);
    return bytes;
}

