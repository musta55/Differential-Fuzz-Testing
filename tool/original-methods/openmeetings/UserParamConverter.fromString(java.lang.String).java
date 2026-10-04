@Override
public UserDTO fromString(String val) {
    JSONObject o = new JSONObject(val);
    if (o.has(ROOT)) {
        o = o.getJSONObject(ROOT);
    }
    return UserDTO.get(o);
}