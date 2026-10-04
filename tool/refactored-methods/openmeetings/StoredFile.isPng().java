public boolean isPng() {
    return mime != null && MIME_PNG.equals(mime);
}
// ---- helper method(s) introduced by the refactoring ----
private void setNameAndExt(String inName, String inExt) {
    if (Strings.isEmpty(inExt)) {
        int idx = inName.lastIndexOf('.');
        name = idx < 0 ? inName : inName.substring(0, idx);
        ext = getFileExt(inName);
    } else {
        name = inName;
        ext = inExt.toLowerCase(Locale.ROOT);
    }
}

private void setMimeType(InputStream is) {
    Metadata md = new Metadata();
    md.add(RESOURCE_NAME_KEY, String.format(FILE_NAME_FMT, name, ext));
    try {
        mime = tika.getDetector().detect(is == null ? null : TikaInputStream.get(is), md);
    } catch (Throwable e) {
        mime = null;
        log.error("Unexpected exception while detecting mime type", e);
    }
}

private boolean isText() {
    return MIME_TEXT.equals(mime.getType());
}

private boolean isOfficeApplication() {
    String subtype = mime.getSubtype();
    return MIME_APP.equals(mime.getType()) && (subtype.startsWith("vnd.oasis.opendocument") || subtype.startsWith("vnd.sun.xml") || subtype.startsWith("vnd.stardivision") || subtype.startsWith("x-star") || subtype.startsWith("vnd.ms-") || subtype.startsWith("vnd.openxmlformats-officedocument"));
}

