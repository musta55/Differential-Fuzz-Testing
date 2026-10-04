/**
 * Converts a number of bytes in a long to a human readable string with GB, MB, KB and B.
 *
 * @param bytes Number of bytes.
 * @return A human readable string
 */
public static String byteMemoryToString(long bytes) {
    if (bytes >= 1024 * 1024 * 1024)
        return String.format("%6d GB", bytes / 1024 / 1024 / 1024);
    else if (bytes >= 1024 * 1024)
        return String.format("%6d MB", bytes / 1024 / 1024);
    else if (bytes >= 1024)
        return String.format("%6d KB", bytes / 1024);
    else
        return String.format("%6d B", bytes);
}