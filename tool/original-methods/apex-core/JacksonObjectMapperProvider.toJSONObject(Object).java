public JSONObject toJSONObject(Object o) {
    try {
        return new JSONObject(this.getContext(null).writeValueAsString(o));
    } catch (Exception ex) {
        throw new RuntimeException(ex);
    }
}