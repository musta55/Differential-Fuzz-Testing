public static <T extends Enum<T>> Collection<T> optEnumList(Class<T> clazz, JSONArray arr) {
    Collection<T> l = new ArrayList<>();
    if (arr != null) {
        for (int i = 0; i < arr.length(); ++i) {
            String enumName = arr.getString(i);
            if (enumName != null) {
                try {
                    l.add(Enum.valueOf(clazz, enumName));
                } catch (IllegalArgumentException e) {
                    // Handle the case where the enum constant does not exist
                }
            }
        }
    }
    return l;
}