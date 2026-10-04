public boolean isImage() {
    if (mime == null) {
        return false;
    }
    return MIME_IMAGE.equals(mime.getType());
}