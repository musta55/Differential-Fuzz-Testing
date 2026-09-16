/**
 * Builds an http document describing an error.
 *
 * @param error       Error name.
 * @param description Errors description.
 * @return A string with the HTML description body
 */
private static String formError(String error, String description) {
    String body = formErrorBody(error, description);
    StringBuilder out = new StringBuilder();
    appendProtocolAndStatus(out, error);
    appendServerHeader(out);
    appendMimeVersionHeader(out);
    appendContentTypeHeader(out, "text/html");
    appendContentLengthHeader(out, body.length());
    appendEmptyLine(out);
    out.append(body);
    return out.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private static void appendProtocolAndStatus(StringBuilder out, String status) {
    out.append(HTTP_PROTOCOL).append(" ").append(status).append(CR);
}

private static void appendServerHeader(StringBuilder out) {
    out.append("Server: ").append(HTTP_SERVER).append(CR);
}

private static void appendMimeVersionHeader(StringBuilder out) {
    out.append("MIME-version: 1.0").append(CR);
}

private static void appendContentTypeHeader(StringBuilder out, String contentType) {
    out.append("Content-Type: ").append(contentType.isEmpty() ? "text/html" : contentType).append(CR);
}

private static void appendContentLengthHeader(StringBuilder out, long contentLength) {
    if (contentLength != 0) {
        out.append("Content-Length: ").append(contentLength).append(CR);
    }
}

private static void appendEmptyLine(StringBuilder out) {
    out.append(CR);
}

