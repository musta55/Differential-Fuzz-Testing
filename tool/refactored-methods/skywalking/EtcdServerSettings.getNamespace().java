public String getNamespace() {
    if (isNullOrEmpty(namespace)) {
        return null;
    }
    return appendTrailingSlashIfMissing(namespace);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isNullOrEmpty(String str) {
    return Strings.isNullOrEmpty(str);
}

private String appendTrailingSlashIfMissing(String str) {
    return str.endsWith("/") ? str : str + "/";
}

