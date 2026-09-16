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
            HeaderElement[] headerElements = null;
            try {
                headerElements = BasicHeaderValueParser.parseElements(contentDisposition, BasicHeaderValueParser.INSTANCE);
            } catch (ParseException e) {
                log.info("Can't parse header {}", contentDisposition, e);
            }
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
                // Find the first empty line of the multipart, it signals end of headers for multipart
                // Agents are supposed to terminate lines in CRLF:
                //$NON-NLS-1$
                int indexEmptyCrLfCrLfLinePos = part.indexOf(CRLFCRLF);
                //$NON-NLS-1$
                int indexEmptyLfLfLinePos = part.indexOf(LFLF);
                String value = null;
                if (indexEmptyCrLfCrLfLinePos > -1) {
                    // CRLF blank line found
                    value = part.substring(indexEmptyCrLfCrLfLinePos + CRLFCRLF.length(), part.lastIndexOf(CRLF));
                } else if (indexEmptyLfLfLinePos > -1) {
                    // LF blank line found
                    value = part.substring(indexEmptyLfLfLinePos + LFLF.length(), part.lastIndexOf(LF));
                }
                this.addNonEncodedArgument(name, value, contentType);
            }
        }
    }
}