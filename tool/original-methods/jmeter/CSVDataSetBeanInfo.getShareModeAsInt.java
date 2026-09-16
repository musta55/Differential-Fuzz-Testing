public static int getShareModeAsInt(String mode) {
    if (mode == null || mode.length() == 0) {
        // default (e.g. if test plan does not have definition)
        return SHARE_ALL;
    }
    for (int i = 0; i < SHARE_TAGS.length; i++) {
        if (SHARE_TAGS[i].equals(mode)) {
            return i;
        }
    }
    return -1;
}