/**
 * Convert byte array to int.
 *
 * @param b
 *            - Byte array to be converted
 * @return Integer value of input byte array
 * @throws IllegalArgumentException if ba is null or not length 2 or 4
 */
public static int byteArrayToInt(byte[] b) {
    if (b == null) {
        throw new IllegalArgumentException("Byte array is null.");
    }
    switch(b.length) {
        case 2:
            return byteArrayToShortInt(b);
        case 4:
            return byteArrayToIntInt(b);
        default:
            throw new IllegalArgumentException("Byte array is invalid length.");
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void validateShortRange(int value) {
    if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
        throw new IllegalArgumentException("Value outside range for signed short int.");
    }
}

private static byte[] intToTwoByteArray(int value) {
    return new byte[] { (byte) (value >> 8), (byte) value };
}

private static byte[] intToFourByteArray(int value) {
    return new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
}

private static int byteArrayToShortInt(byte[] b) {
    return (b[0] << 8) | (b[1] & 0xFF);
}

private static int byteArrayToIntInt(byte[] b) {
    return (b[0] << 24) | ((b[1] & 0xFF) << 16) | ((b[2] & 0xFF) << 8) | (b[3] & 0xFF);
}

