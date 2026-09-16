public JSONArray clearSlide(int slide) {
    JSONArray arr = new JSONArray();
    roomItems.entrySet().removeIf(e -> {
        JSONObject o = new JSONObject(e.getValue());
        boolean match = !BaseFileItem.Type.PRESENTATION.name().equals(o.optString(ATTR_FILE_TYPE)) && o.optInt(ATTR_SLIDE, -1) == slide;
        if (match) {
            arr.put(e);
        }
        return match;
    });
    return arr;
}