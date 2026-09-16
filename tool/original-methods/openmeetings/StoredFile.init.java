private void init(String inName, String inExt, InputStream is) {
    if (Strings.isEmpty(inExt)) {
        int idx = inName.lastIndexOf('.');
        name = idx < 0 ? inName : inName.substring(0, idx);
        ext = getFileExt(inName);
    } else {
        name = inName;
        ext = inExt.toLowerCase(Locale.ROOT);
    }
    Metadata md = new Metadata();
    md.add(RESOURCE_NAME_KEY, String.format(FILE_NAME_FMT, name, ext));
    try {
        mime = tika.getDetector().detect(is == null ? null : TikaInputStream.get(is), md);
    } catch (Throwable e) {
        mime = null;
        log.error("Unexpected exception while detecting mime type", e);
    }
}