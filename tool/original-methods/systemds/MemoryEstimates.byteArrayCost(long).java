/**
 * Get the worst case memory usage of an array of bytes.
 *
 * @param length The length of the array.
 * @return The memory estimate in bytes
 */
public static double byteArrayCost(long length) {
    long size = 0;
    // Byte array Reference
    size += 8;
    // Byte array Object header
    size += 20;
    if (length <= 4) {
        // byte array fills out the first 4 bytes differently than the later bytes.
        size += 4;
    } else {
        // byte array pads to next 8 bytes after the first 4.
        size += length;
        double diff = (length - 4) % 8;
        if (diff > 0) {
            size += 8 - diff;
        }
    }
    return size;
}