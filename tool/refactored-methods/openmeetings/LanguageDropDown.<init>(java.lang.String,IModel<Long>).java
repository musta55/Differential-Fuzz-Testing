public LanguageDropDown(String id, IModel<Long> model) {
    this(id);
    setModel(model);
}
// ---- helper method(s) introduced by the refactoring ----
private void initChoices() {
    for (Map.Entry<Long, Locale> e : LabelDao.getLanguages()) {
        languages.add(e.getKey());
    }
    setChoices(languages);
}

private String getLocaleDisplayName(Long id) {
    return LabelDao.getLocale(id).getDisplayName();
}

