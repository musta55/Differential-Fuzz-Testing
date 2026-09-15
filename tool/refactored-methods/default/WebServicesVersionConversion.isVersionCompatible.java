public static boolean isVersionCompatible(String version) {
    if (!version.startsWith("v")) {
        LOG.error("Version {} is invalid", version);
        return false;
    }
    version = version.substring(1);
    int majorVersion = parseMajorVersion(version);
    int thisVersion = parseMajorVersion(WebServices.VERSION);
    if (majorVersion > thisVersion) {
        LOG.warn("Future stram version {} is incompatible. Please upgrade the DataTorrent Gateway and/or CLI", majorVersion);
        return false;
    }
    // Add old versions that are not supported here in the future
    return true;
}
// ---- helper method(s) introduced by the refactoring ----
private static int parseMajorVersion(String version) {
    return Integer.parseInt(version.split("\\.")[0]);
}

