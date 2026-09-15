public PermissionsInfo(JSONObject json) throws JSONException {
    JSONObject readOnly = json.optJSONObject("readOnly");
    JSONObject readWrite = json.optJSONObject("readWrite");
    if (readOnly != null) {
        JSONArray users = readOnly.optJSONArray("users");
        if (users != null) {
            for (int i = 0; i < users.length(); i++) {
                readOnlyUsers.add(users.getString(i));
            }
        }
        JSONArray roles = readOnly.optJSONArray("roles");
        if (roles != null) {
            for (int i = 0; i < roles.length(); i++) {
                readOnlyRoles.add(roles.getString(i));
            }
        }
        readOnlyEveryone = readOnly.optBoolean("everyone", false);
    }
    if (readWrite != null) {
        JSONArray users = readWrite.optJSONArray("users");
        if (users != null) {
            for (int i = 0; i < users.length(); i++) {
                readWriteUsers.add(users.getString(i));
            }
        }
        JSONArray roles = readWrite.optJSONArray("roles");
        if (roles != null) {
            for (int i = 0; i < roles.length(); i++) {
                readWriteRoles.add(roles.getString(i));
            }
        }
        readWriteEveryone = readWrite.optBoolean("everyone", false);
    }
}