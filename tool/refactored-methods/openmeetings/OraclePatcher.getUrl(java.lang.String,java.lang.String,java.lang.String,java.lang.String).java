@Override
protected String getUrl(String inUrl, String host, String inPort, String inDb) {
    String port = getDefaultPort(inPort);
    String db = getDefaultDbName(inDb);
    String delimiter = getDelimiter(db);
    return constructUrl(host, port, delimiter, db);
}
// ---- helper method(s) introduced by the refactoring ----
private String getDefaultPort(String inPort) {
    return (inPort == null) ? "1521" : inPort;
}

private String getDefaultDbName(String inDb) {
    return (inDb == null) ? DEFAULT_DB_NAME : inDb;
}

private String getDelimiter(String db) {
    return db.startsWith("/") ? "" : ":";
}

private String constructUrl(String host, String port, String delimiter, String db) {
    return String.format("jdbc:oracle:thin:@%s:%s%s%s", host, port, delimiter, db);
}

