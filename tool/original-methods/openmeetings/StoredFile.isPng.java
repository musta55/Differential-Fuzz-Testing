public boolean isPng() {
    if (mime == null) {
        return false;
    }
    return MIME_PNG.equals(mime);
}