public static RemoteSessionObject fromString(String s) {
    JSONObject o = new JSONObject(s);
    RemoteSessionObject ro = new RemoteSessionObject();
    ro.username = o.optString("username");
    ro.firstname = o.optString("firstname");
    ro.lastname = o.optString("lastname");
    ro.pictureUrl = o.optString("pictureUrl");
    ro.email = o.optString("email");
    ro.externalId = o.getString("externalId");
    ro.externalType = o.getString("externalType");
    return ro;
}