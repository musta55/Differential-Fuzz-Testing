@Override
public UserDTO fromString(String val) {
    JSONObject jsonObject = parseJSONObject(val);
    return UserDTO.get(jsonObject);
}
// ---- helper method(s) introduced by the refactoring ----
private JSONObject parseJSONObject(String val) {
    JSONObject o = new JSONObject(val);
    if (o.has(ROOT)) {
        o = o.getJSONObject(ROOT);
    }
    return o;
}

