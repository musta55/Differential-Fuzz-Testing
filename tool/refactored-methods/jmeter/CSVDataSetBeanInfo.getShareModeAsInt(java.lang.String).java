public static int getShareModeAsInt(String mode) {
    if (mode == null || mode.isEmpty()) {
        // default (e.g. if test plan does not have definition)
        return SHARE_ALL;
    }
    return SHARE_MODE_MAP.getOrDefault(mode, -1);
}