/**
 * builds an http document describing an error.
 *
 * @param error       Error name.
 * @param description Errors description.
 * @return A string with the HTML description body
 */
private static String formError(String error, String description) {
    /*
         * A HTTP RESPONSE HEADER LOOKS LIKE:
         *
         * HTTP/1.0 200 OK Date: Wednesday, 02-Feb-94 23:04:12 GMT Server:
         * NCSA/1.1 MIME-version: 1.0 Last-modified: Monday, 15-Nov-93 23:33:16
         * GMT Content-Type: text/html Content-Length: 2345 \r\n
         */
    String body = formErrorBody(error, description);
    return HTTP_PROTOCOL + " " + error + CR + "Server: " + HTTP_SERVER + CR + "MIME-version: 1.0" + CR + "Content-Type: text/html" + CR + "Content-Length: " + body.length() + CR + CR + body;
}