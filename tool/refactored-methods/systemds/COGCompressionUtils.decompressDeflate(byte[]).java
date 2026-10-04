/**
 * Decompresses a byte array that was compressed using the Deflate algorithm.
 *
 * @param compressedData the byte array containing the compressed data
 * @return the decompressed byte array
 * @throws DMLRuntimeException if the decompression fails due to a data format error
 */
public static byte[] decompressDeflate(byte[] compressedData) throws DMLRuntimeException {
    Inflater inflater = new Inflater();
    inflater.setInput(compressedData);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedData.length);
    byte[] buffer = new byte[1024];
    while (!inflater.finished()) {
        int decompressedSize = inflateData(inflater, buffer);
        outputStream.write(buffer, 0, decompressedSize);
    }
    inflater.end();
    return outputStream.toByteArray();
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Inflates data from the input stream into the buffer using the specified inflater.
 *
 * @param inflater the inflater used for decompression
 * @param buffer   the buffer to store the decompressed data
 * @return the number of bytes decompressed and written to the buffer
 * @throws DMLRuntimeException if a data format error occurs during inflation
 */
private static int inflateData(Inflater inflater, byte[] buffer) throws DMLRuntimeException {
    try {
        return inflater.inflate(buffer);
    } catch (DataFormatException e) {
        throw new DMLRuntimeException("Failed to decompress tile data", e);
    }
}

