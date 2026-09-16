@Override
protected String getUrl(String inUrl, String host, String inPort, String inDb) {
    String port = (inPort == null) ? "1521" : inPort;
    String db = (inDb == null) ? DEFAULT_DB_NAME : inDb;
    String delim = ":";
    if (db.startsWith("/")) {
        delim = "";
    }
    return String.format("jdbc:oracle:thin:@%s:%s%s%s", host, port, delim, db);
}