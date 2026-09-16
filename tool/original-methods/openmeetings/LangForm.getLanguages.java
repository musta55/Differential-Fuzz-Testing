static List<Map.Entry<Long, Locale>> getLanguages() {
    List<Map.Entry<Long, Locale>> list = new ArrayList<>();
    for (Map.Entry<Long, Locale> e : LabelDao.getLanguages()) {
        list.add(new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()));
    }
    return list;
}