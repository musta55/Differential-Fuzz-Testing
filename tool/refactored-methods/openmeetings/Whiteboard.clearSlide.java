public JSONArray clearSlide(int slide) {
    JSONArray arr = new JSONArray();
    roomItems.entrySet().removeIf(entry -> shouldRemoveEntry(entry, slide, arr));
    return arr;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean shouldRemoveEntry(Entry<String, String> entry, int slide, JSONArray arr) {
    JSONObject o = new JSONObject(entry.getValue());
    boolean match = !BaseFileItem.Type.PRESENTATION.name().equals(o.optString(ATTR_FILE_TYPE)) && o.optInt(ATTR_SLIDE, -1) == slide;
    if (match) {
        arr.put(entry);
    }
    return match;
}

private JSONObject createDeepCopy() {
    return new JSONObject(new JSONObject(this).toString(new NullStringer()));
}

private void filterJson(JSONObject json) {
    json.remove("id");
    json.remove("empty");
    json.put(ITEMS_KEY, filterRoomItems());
}

private JSONObject filterRoomItems() {
    JSONObject items = new JSONObject();
    for (Entry<String, String> e : roomItems.entrySet()) {
        JSONObject o = new JSONObject(e.getValue());
        filterItem(o);
        items.put(e.getKey(), o);
    }
    return items;
}

private void filterItem(JSONObject o) {
    if ("Clipart".equals(o.opt(ATTR_OMTYPE))) {
        if (o.has(PARAM_SRC_UND)) {
            o.put(PARAM_SRC, o.get(PARAM_SRC_UND));
        }
    } else {
        o.remove(PARAM_SRC);
    }
    o.remove(PARAM_SRC_UND);
}

