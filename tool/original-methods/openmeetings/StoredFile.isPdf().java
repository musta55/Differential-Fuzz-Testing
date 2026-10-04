public boolean isPdf() {
    if (mime == null) {
        return false;
    }
    return PDF_TYPES.contains(mime);
}