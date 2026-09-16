public static RemoteSessionObject fromString(String s) {
    JSONObject o = new JSONObject(s);
    RemoteSessionObjectBuilder builder = new RemoteSessionObjectBuilder().username(o.optString("username")).firstname(o.optString("firstname")).lastname(o.optString("lastname")).pictureUrl(o.optString("pictureUrl")).email(o.optString("email")).externalId(o.getString("externalId")).externalType(o.getString("externalType"));
    return builder.build();
}