public JSONObject toJSONObject(Object o) {
    try {
        return new JSONObject(this.getContext(null).writeValueAsString(o));
    } catch (IOException | JSONException ex) {
        throw new RuntimeException(ex);
    }
}