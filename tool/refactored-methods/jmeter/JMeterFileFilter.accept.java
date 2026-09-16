/**
 * Determine if the specified filename is allowed by this filter. The file
 * will be allowed if the end of the filename matches one of the extensions
 * allowed by this filter. The comparison is case-insensitive. If no
 * extensions were provided for this filter, the file will always be
 * allowed.
 *
 * @param filename
 *            the filename to test
 * @return true if the file should be allowed, false otherwise
 */
public boolean accept(String filename) {
    return Arrays.stream(exts).anyMatch(ext -> filename.toLowerCase(Locale.ROOT).endsWith(ext.toLowerCase(Locale.ROOT)));
}