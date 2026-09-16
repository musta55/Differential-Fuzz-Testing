public boolean isAudio() {
    if (mime == null) {
        return false;
    }
    return MIME_AUDIO.equals(mime.getType());
}