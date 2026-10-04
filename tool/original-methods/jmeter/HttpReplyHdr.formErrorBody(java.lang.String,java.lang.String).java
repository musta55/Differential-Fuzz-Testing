/**
 * private! builds an http document describing a headers reason.
 *
 * @param error
 *            Error name.
 * @param description
 *            Errors description.
 * @return A string with the HTML description body
 */
private static String formErrorBody(String error, String description) {
    StringBuilder out = new StringBuilder();
    // Generate Error Body
    out.append("<HTML><HEAD><TITLE>");
    out.append(error);
    out.append("</TITLE></HEAD>");
    out.append("<BODY><H2>").append(error).append("</H2>\n");
    out.append("</P></H3>");
    out.append(description);
    out.append("</BODY></HTML>");
    return out.toString();
}