/**
 * This method allows a proxy server to send over the raw text from a
 * browser's output stream to be parsed and stored correctly into the
 * UrlConfig object.
 *
 * @param queryString text to parse
 */
public void parseArguments(String queryString) {
    //$NON-NLS-1$
    String[] parts = JOrphanUtils.split(queryString, "--" + getBoundary());
    for (String part : parts) {
        //$NON-NLS-1$
        String contentDisposition = getHeaderValue("Content-disposition", part);
        //$NON-NLS-1$
        String contentType = getHeaderValue("Content-type", part);
        // Check if it is form data
        if (contentDisposition != null && contentDisposition.contains("form-data")) {
            //$NON-NLS-1$
            // Get the form field name
            HeaderElement[] headerElements = parseHeaderElements(contentDisposition);
            String name = "";
            String path = null;
            if (headerElements != null) {
                for (HeaderElement element : headerElements) {
                    name = getParameterValue(element, "name", "");
                    path = getParameterValue(element, "filename", null);
                }
            }
            if (path != null && !path.isEmpty()) {
                // Set the values retrieved for the file upload
                files.addHTTPFileArg(path, name, contentType);
            } else {
                String value = extractValue(part);
                addNonEncodedArgument(args, name, value, contentType);
            }
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Add a value that is not URL encoded, and make sure it
 * appears in the GUI that it will not be encoded when
 * the request is sent.
 *
 * @param name
 * @param value
 * @param contentType can include charset or not, for example:  "application/json; charset=UTF-8" or  "application/json"
 */
private static void addNonEncodedArgument(Arguments arguments, String name, String value, String contentType) {
    // The value is not encoded
    HTTPArgument arg = new HTTPArgument(name, value, false);
    if (!StringUtils.isEmpty(contentType)) {
        arg.setContentType(extractContentType(contentType));
    }
    // Let the GUI show that it will not be encoded
    arg.setAlwaysEncoded(false);
    arguments.addArgument(arg);
}

private static String extractContentType(String contentType) {
    int indexOfSemiColon = contentType.indexOf(';');
    return indexOfSemiColon > 0 ? contentType.substring(0, indexOfSemiColon) : contentType;
}

private static HeaderElement[] parseHeaderElements(String contentDisposition) {
    try {
        return BasicHeaderValueParser.parseElements(contentDisposition, BasicHeaderValueParser.INSTANCE);
    } catch (ParseException e) {
        log.info("Can't parse header {}", contentDisposition, e);
        return null;
    }
}

private static String extractValue(String part) {
    //$NON-NLS-1$
    int indexEmptyCrLfCrLfLinePos = part.indexOf(CRLFCRLF);
    //$NON-NLS-1$
    int indexEmptyLfLfLinePos = part.indexOf(LFLF);
    if (indexEmptyCrLfCrLfLinePos > -1) {
        // CRLF blank line found
        return part.substring(indexEmptyCrLfCrLfLinePos + CRLFCRLF.length(), part.lastIndexOf(CRLF));
    } else if (indexEmptyLfLfLinePos > -1) {
        // LF blank line found
        return part.substring(indexEmptyLfLfLinePos + LFLF.length(), part.lastIndexOf(LF));
    }
    return null;
}

