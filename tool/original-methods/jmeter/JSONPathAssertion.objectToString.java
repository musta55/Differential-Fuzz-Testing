public static String objectToString(Object subj) {
    String str;
    if (subj == null) {
        str = "null";
    } else if (subj instanceof Map) {
        //noinspection unchecked
        str = new JSONObject((Map<String, ?>) subj).toJSONString();
    } else if (subj instanceof Double || subj instanceof Float) {
        str = decimalFormatter.get().format(subj);
    } else {
        str = subj.toString();
    }
    return str;
}