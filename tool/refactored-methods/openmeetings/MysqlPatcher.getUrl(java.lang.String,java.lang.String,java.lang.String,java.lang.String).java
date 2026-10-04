@Override
protected String getUrl(String inUrl, String host, String inPort, String inDb) {
    Url url = Url.parse(inUrl);
    setHost(url, host);
    setPort(url, inPort);
    setDatabase(url, inDb);
    setTimezone(url);
    return url.toString(Url.StringMode.FULL);
}
// ---- helper method(s) introduced by the refactoring ----
private void setHost(Url url, String host) {
    url.setHost(host);
}

private void setPort(Url url, String inPort) {
    url.setPort((inPort == null) ? 3306 : Integer.valueOf(inPort));
}

private void setDatabase(Url url, String inDb) {
    url.getSegments().set(1, (inDb == null) ? DEFAULT_DB_NAME : inDb);
}

private void setTimezone(Url url) {
    PageParameters pp = new PageParametersEncoder().decodePageParameters(url);
    StringValue tz = pp.get(TZ_PARAM);
    if (tz.isEmpty()) {
        url.setQueryParameter(TZ_PARAM, TimeZone.getDefault().getID());
    }
}

