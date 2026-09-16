public boolean isOffice() {
    if (mime == null) {
        return false;
    }
    return MIME_TEXT.equals(mime.getType()) || (MIME_APP.equals(mime.getType()) && (mime.getSubtype().startsWith("vnd.oasis.opendocument") || mime.getSubtype().startsWith("vnd.sun.xml") || mime.getSubtype().startsWith("vnd.stardivision") || mime.getSubtype().startsWith("x-star") || mime.getSubtype().startsWith("vnd.ms-") || mime.getSubtype().startsWith("vnd.openxmlformats-officedocument"))) || CONVERT_TYPES.contains(mime);
}