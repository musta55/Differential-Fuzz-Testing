public H5DoubleDataType(ByteBuffer bb) {
    // Class and version
    BitSet classAndVersion = BitSet.valueOf(readBytes(bb, CLASS_AND_VERSION_BYTE_SIZE));
    dataClass = Utils.bitsToInt(classAndVersion, CLASS_VERSION_BIT_OFFSET, CLASS_VERSION_BIT_LENGTH);
    version = Utils.bitsToInt(classAndVersion, 0, CLASS_VERSION_BIT_LENGTH);
    classBits = BitSet.valueOf(readBytes(bb, CLASS_BITS_BYTE_SIZE));
    // Size
    size = Utils.readBytesAsUnsignedInt(bb, SIZE_INT_BYTE_SIZE);
    if (classBits.get(VAX_ENDIAN_FLAG)) {
        throw new H5RuntimeException("VAX endian is not supported");
    }
    order = classBits.get(ORDER_BIT_INDEX) ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
    lowPadding = classBits.get(LOW_PADDING_BIT_INDEX);
    highPadding = classBits.get(HIGH_PADDING_BIT_INDEX);
    internalPadding = classBits.get(INTERNAL_PADDING_BIT_INDEX);
    // Mask the 4+5 bits and shift to the end
    mantissaNormalization = Utils.bitsToInt(classBits, MANTISSA_NORMALIZATION_BIT_OFFSET, MANTISSA_NORMALIZATION_BIT_LENGTH);
    signLocation = Utils.bitsToInt(classBits, SIGN_LOCATION_BIT_OFFSET, SIGN_LOCATION_BIT_LENGTH);
    // Properties
    bitOffset = bb.getShort();
    bitPrecision = bb.getShort();
    exponentLocation = bb.get();
    exponentSize = bb.get();
    mantissaLocation = bb.get();
    mantissaSize = bb.get();
    exponentBias = bb.getInt();
}
// ---- helper method(s) introduced by the refactoring ----
private byte[] readBytes(ByteBuffer bb, int length) {
    byte[] bytes = new byte[length];
    bb.get(bytes);
    return bytes;
}

