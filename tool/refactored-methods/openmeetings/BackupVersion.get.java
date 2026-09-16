public static BackupVersion get(String ver) {
    return parseVersion(ver);
}
// ---- helper method(s) introduced by the refactoring ----
private static BackupVersion parseVersion(String ver) {
    BackupVersion bv = new BackupVersion();
    String[] dashParts = ver.split("-");
    if (dashParts.length == 0) {
        return bv;
    }
    String[] dotParts = dashParts[0].split("\\.");
    setVersionPart(dotParts, 0, bv::setMajor);
    setVersionPart(dotParts, 1, bv::setMinor);
    setVersionPart(dotParts, 2, bv::setMicro);
    return bv;
}

private static void setVersionPart(String[] parts, int index, IntConsumer setter) {
    if (index < parts.length) {
        setter.accept(toInt(parts[index]));
    }
}

