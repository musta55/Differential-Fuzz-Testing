/**
 * Convert int to byte array.
 *
 * @param value
 *            - int to be converted
 * @param len
 *            - length of required byte array
 * @return Byte array representation of input value
 * @throws IllegalArgumentException if not length 2 or 4 or outside range of a short int.
 */
public static byte[] intToByteArray(int value, int len) {
    switch(len) {
        case 2:
            validateShortRange(value);
            return intToTwoByteArray(value);
        case 4:
            return intToFourByteArray(value);
        default:
            throw new IllegalArgumentException("Length must be specified as either 2 or 4.");
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

