public void addReadWriteUser(String user) {
    readWriteUsers.add(user);
}
// ---- helper method(s) introduced by the refactoring ----
private void parseReadOnly(JSONObject readOnly) throws JSONException {
    if (readOnly != null) {
        parseUsers(readOnlyUsers, readOnly.optJSONArray("users"));
        parseRoles(readOnlyRoles, readOnly.optJSONArray("roles"));
        readOnlyEveryone = readOnly.optBoolean("everyone", false);
    }
}

private void parseReadWrite(JSONObject readWrite) throws JSONException {
    if (readWrite != null) {
        parseUsers(readWriteUsers, readWrite.optJSONArray("users"));
        parseRoles(readWriteRoles, readWrite.optJSONArray("roles"));
        readWriteEveryone = readWrite.optBoolean("everyone", false);
    }
}

private void parseUsers(Set<String> users, JSONArray jsonArray) throws JSONException {
    if (jsonArray != null) {
        for (int i = 0; i < jsonArray.length(); i++) {
            users.add(jsonArray.getString(i));
        }
    }
}

private void parseRoles(Set<String> roles, JSONArray jsonArray) throws JSONException {
    if (jsonArray != null) {
        for (int i = 0; i < jsonArray.length(); i++) {
            roles.add(jsonArray.getString(i));
        }
    }
}

private JSONObject createPermissionObject(Set<String> users, Set<String> roles, boolean everyone) throws JSONException {
    JSONObject permissionObject = new JSONObject();
    permissionObject.put("users", new JSONArray(users));
    permissionObject.put("roles", new JSONArray(roles));
    permissionObject.put("everyone", everyone);
    return permissionObject;
}

