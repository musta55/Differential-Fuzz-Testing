/**
 * Generate a scheme with the given type of columnGroup and number of columns in each group
 *
 * @param type  The type of encoding to use
 * @param nCols The number of columns
 * @return A scheme to generate.
 */
public static CompressionScheme genScheme(CompressionType type, int nCols) {
    ICLAScheme[] encodings = new ICLAScheme[nCols];
    for (int i = 0; i < nCols; i++) {
        encodings[i] = SchemeFactory.create(new SingleIndex(i), type);
    }
    return new CompressionScheme(encodings);
}