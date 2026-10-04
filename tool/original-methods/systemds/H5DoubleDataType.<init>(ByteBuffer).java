public H5DoubleDataType(ByteBuffer bb) {
    // Class and version
    final BitSet classAndVersion = BitSet.valueOf(new byte[] { bb.get() });
    dataClass = Utils.bitsToInt(classAndVersion, 0, 4);
    version = Utils.bitsToInt(classAndVersion, 4, 4);
    byte[] classBytes = new byte[3];
    bb.get(classBytes);
    classBits = BitSet.valueOf(classBytes);
    // Size
    size = Utils.readBytesAsUnsignedInt(bb, 4);
    if (classBits.get(6)) {
        throw new H5RuntimeException("VAX endian is not supported");
    }
    if (classBits.get(0)) {
        order = ByteOrder.BIG_ENDIAN;
    } else {
        order = ByteOrder.LITTLE_ENDIAN;
    }
    lowPadding = classBits.get(1);
    highPadding = classBits.get(2);
    internalPadding = classBits.get(3);
    // Mask the 4+5 bits and shift to the end
    mantissaNormalization = Utils.bitsToInt(classBits, 4, 2);
    signLocation = Utils.bitsToInt(classBits, 8, 8);
    // Properties
    bitOffset = bb.getShort();
    bitPrecision = bb.getShort();
    exponentLocation = bb.get();
    exponentSize = bb.get();
    mantissaLocation = bb.get();
    mantissaSize = bb.get();
    exponentBias = bb.getInt();
}