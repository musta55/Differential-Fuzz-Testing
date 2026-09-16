/**
 * Convert byte array to int.
 *
 * @param b
 *            - Byte array to be converted
 * @return Integer value of input byte array
 * @throws IllegalArgumentException if ba is null or not length 2 or 4
 */
public static int byteArrayToInt(byte[] b) {
    if (b != null && (b.length == 2 || b.length == 4)) {
        // Preserve sign on first byte
        int value = b[0] << ((b.length - 1) * 8);
        for (int i = 1; i < b.length; i++) {
            int offset = (b.length - 1 - i) * 8;
            value += (b[i] & 0xFF) << offset;
        }
        return value;
    } else {
        throw new IllegalArgumentException("Byte array is null or invalid length.");
    }
}