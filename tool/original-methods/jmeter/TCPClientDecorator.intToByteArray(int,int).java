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
    if (len == 2 || len == 4) {
        if (len == 2 && (value < Short.MIN_VALUE || value > Short.MAX_VALUE)) {
            throw new IllegalArgumentException("Value outside range for signed short int.");
        } else {
            byte[] b = new byte[len];
            for (int i = 0; i < len; i++) {
                int offset = (b.length - 1 - i) * 8;
                b[i] = (byte) ((value >>> offset) & 0xFF);
            }
            return b;
        }
    } else {
        throw new IllegalArgumentException("Length must be specified as either 2 or 4.");
    }
}