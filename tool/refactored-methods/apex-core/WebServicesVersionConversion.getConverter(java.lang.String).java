public static Converter getConverter(String version) throws IncompatibleVersionException {
    if (!isVersionCompatible(version)) {
        throw new IncompatibleVersionException("Stram version " + version + " is incompatible with the current build (" + WebServices.VERSION + ")");
    }
    // Add old versions that ARE supported here in the future
    switch(version) {
        case "v1":
            return new V1Converter();
        default:
            return null;
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static int parseMajorVersion(String version) {
    return Integer.parseInt(version.split("\\.")[0]);
}

