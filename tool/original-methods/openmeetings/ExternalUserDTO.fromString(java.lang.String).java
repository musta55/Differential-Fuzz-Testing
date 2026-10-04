public static ExternalUserDTO fromString(String s) {
    JSONObject o = new JSONObject(s);
    ExternalUserDTO u = new ExternalUserDTO();
    u.email = o.optString("email", null);
    u.externalId = o.optString("externalId", null);
    u.externalType = o.optString("externalType", null);
    u.firstname = o.optString("firstname", null);
    u.lastname = o.optString("lastname", null);
    u.login = o.optString("login", null);
    u.profilePictureUrl = o.optString("profilePictureUrl", null);
    return u;
}