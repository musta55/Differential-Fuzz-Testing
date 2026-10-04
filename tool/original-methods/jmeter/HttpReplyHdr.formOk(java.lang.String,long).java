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
    out.append(HTTP_PROTOCOL).append(" 200 Ok").append(CR);
    out.append("Server: ").append(HTTP_SERVER).append(CR);
    out.append("MIME-version: 1.0").append(CR);
    if (0 < contentType.length()) {
        out.append("Content-Type: ").append(contentType).append(CR);
    } else {
        out.append("Content-Type: text/html").append(CR);
    }
    if (0 != contentLength) {
        out.append("Content-Length: ").append(contentLength).append(CR);
    }
    out.append(CR);
    return out.toString();
}