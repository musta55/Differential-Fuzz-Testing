public JSONObject toJson() {
    //deep-copy
    JSONObject json = new JSONObject(new JSONObject(this).toString(new NullStringer()));
    //filtering
    json.remove("id");
    //filtering
    json.remove("empty");
    JSONObject items = new JSONObject();
    for (Entry<String, String> e : roomItems.entrySet()) {
        JSONObject o = new JSONObject(e.getValue());
        //filtering
        if ("Clipart".equals(o.opt(ATTR_OMTYPE))) {
            if (o.has(PARAM_SRC_UND)) {
                o.put(PARAM_SRC, o.get(PARAM_SRC_UND));
            }
        } else {
            o.remove(PARAM_SRC);
        }
        o.remove(PARAM_SRC_UND);
        items.put(e.getKey(), o);
    }
    json.put(ITEMS_KEY, items);
    return json;
}