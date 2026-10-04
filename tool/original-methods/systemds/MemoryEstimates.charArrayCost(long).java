/**
 * Get the worst case memory usage of an array of chars.
 *
 * @param length The length of the array.
 * @return The memory estimate in bytes
 */
public static double charArrayCost(long length) {
    double size = 0;
    // char array Reference
    size += 8;
    // char array Object header
    size += 20;
    if (length <= 2) {
        // char array fills out the first 2 chars differently than the later bytes.
        size += 4;
    } else {
        // 2 bytes per char
        size += length * 2;
        double diff = (length * 2 - 4) % 8;
        if (diff > 0) {
            // next object alignment
            size += 8 - diff;
        }
    }
    return size;
}