public JSONObject toJSONObject() {
    JSONObject result = new JSONObject();
    JSONObject readOnly = new JSONObject();
    JSONObject readWrite = new JSONObject();
    try {
        readOnly.put("users", new JSONArray(readOnlyUsers));
        readOnly.put("roles", new JSONArray(readOnlyRoles));
        readOnly.put("everyone", readOnlyEveryone);
        readWrite.put("users", new JSONArray(readWriteUsers));
        readWrite.put("roles", new JSONArray(readWriteRoles));
        readWrite.put("everyone", readWriteEveryone);
        result.put("readOnly", readOnly);
        result.put("readWrite", readWrite);
    } catch (JSONException ex) {
        throw new RuntimeException(ex);
    }
    return result;
}