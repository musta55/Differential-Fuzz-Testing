public boolean isAsIs() {
    if (mime == null) {
        return false;
    }
    return AS_IS_TYPES.contains(mime);
}