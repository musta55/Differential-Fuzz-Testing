@Override
protected String getUrl(String inUrl, String host, String inPort, String inDb) {
    Url url = Url.parse(inUrl);
    url.setHost(host);
    url.setPort((inPort == null) ? 3306 : Integer.valueOf(inPort));
    url.getSegments().set(1, (inDb == null) ? DEFAULT_DB_NAME : inDb);
    PageParameters pp = new PageParametersEncoder().decodePageParameters(url);
    StringValue tz = pp.get(TZ_PARAM);
    if (tz.isEmpty()) {
        url.setQueryParameter(TZ_PARAM, TimeZone.getDefault().getID());
    }
    return url.toString(Url.StringMode.FULL);
}