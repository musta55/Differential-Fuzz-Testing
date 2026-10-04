public static long readBytesAsUnsignedLong(ByteBuffer buffer, int length) {
    switch(length) {
        case 1:
            return Byte.toUnsignedLong(buffer.get());
        case 2:
            return Short.toUnsignedLong(buffer.getShort());
        case 3:
            return readArbitraryLengthBytesAsUnsignedLong(buffer, length);
        case 4:
            return Integer.toUnsignedLong(buffer.getInt());
        case 5:
        case 6:
        case 7:
            return readArbitraryLengthBytesAsUnsignedLong(buffer, length);
        case 8:
            long value = buffer.getLong();
            if (value < 0 && value != H5Constants.UNDEFINED_ADDRESS) {
                throw new ArithmeticException("Could not convert to unsigned");
            }
            return value;
        default:
            throw new IllegalArgumentException("Couldn't read " + length + " bytes as int");
    }
}