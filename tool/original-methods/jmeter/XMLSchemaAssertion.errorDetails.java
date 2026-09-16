// Helper method to construct SAX error details
private static String errorDetails(SAXParseException spe) {
    StringBuilder str = new StringBuilder(80);
    int i;
    i = spe.getLineNumber();
    if (i != -1) {
        str.append("line=");
        str.append(i);
        str.append(" col=");
        str.append(spe.getColumnNumber());
        str.append(" ");
    }
    str.append(spe.getLocalizedMessage());
    return str.toString();
}