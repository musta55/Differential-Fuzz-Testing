public boolean isVideo() {
    if (mime == null) {
        return false;
    }
    return isAudio() || MIME_VIDEO.equals(mime.getType());
}