/**
 * Forms a http ok reply header
 *
 * @param contentType
 *            the mime-type of the content
 * @param contentLength
 *            the length of the content
 * @return a string with the header in it
 */
public static String formOk(String contentType, long contentLength) {
    StringBuilder out = new StringBuilder();
    appendProtocolAndStatus(out, "200 Ok");
    appendServerHeader(out);
    appendMimeVersionHeader(out);
    appendContentTypeHeader(out, contentType);
    appendContentLengthHeader(out, contentLength);
    appendEmptyLine(out);
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

