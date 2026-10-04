public static <T extends Enum<T>> Collection<T> optEnumList(Class<T> clazz, JSONArray arr) {
    Collection<T> l = new ArrayList<>();
    if (arr != null) {
        for (int i = 0; i < arr.length(); ++i) {
            l.add(Enum.valueOf(clazz, arr.getString(i)));
        }
    }
    return l;
}