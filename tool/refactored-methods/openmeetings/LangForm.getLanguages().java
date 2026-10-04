static List<Map.Entry<Long, Locale>> getLanguages() {
    return LabelDao.getLanguages().stream().map(e -> Map.entry(e.getKey(), e.getValue())).collect(Collectors.toList());
}