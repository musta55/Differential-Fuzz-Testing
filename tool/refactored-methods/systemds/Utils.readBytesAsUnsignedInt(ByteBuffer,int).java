public static int readBytesAsUnsignedInt(ByteBuffer buffer, int length) {
    switch(length) {
        case 1:
            return Byte.toUnsignedInt(buffer.get());
        case 2:
            return Short.toUnsignedInt(buffer.getShort());
        case 3:
            return readThreeByteUnsignedInt(buffer);
        case 4:
            int value = buffer.getInt();
            if (value < 0) {
                throw new ArithmeticException("Could not convert to unsigned");
            }
            return value;
        case 5:
        case 6:
        case 7:
            return readArbitraryLengthBytesAsUnsignedInt(buffer, length);
        case 8:
            // Throws if the long can't be converted safely
            return Math.toIntExact(buffer.getLong());
        default:
            throw new IllegalArgumentException("Couldn't read " + length + " bytes as int");
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static int readThreeByteUnsignedInt(ByteBuffer buffer) {
    return readArbitraryLengthBytesAsUnsignedInt(buffer, 3);
}

private static long readThreeByteUnsignedLong(ByteBuffer buffer) {
    return readArbitraryLengthBytesAsUnsignedLong(buffer, 3);
}

