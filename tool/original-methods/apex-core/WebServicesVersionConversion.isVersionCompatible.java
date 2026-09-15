public static boolean isVersionCompatible(String version) {
    if (!version.startsWith("v")) {
        LOG.error("Version {} is invalid", version);
        return false;
    }
    version = version.substring(1);
    int majorVersion = Integer.valueOf(version.split("\\.")[0]);
    int thisVersion = Integer.valueOf(WebServices.VERSION.substring(1).split("\\.")[0]);
    if (majorVersion > thisVersion) {
        LOG.warn("Future stram version {} is incompatible. Please upgrade the DataTorrent Gateway and/or CLI", majorVersion);
        return false;
    }
    // Add old versions that are not supported here in the future
    return true;
}