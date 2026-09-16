public LanguageDropDown(String id) {
    super(id, new ArrayList<>());
    initChoices();
    setChoiceRenderer(new LambdaChoiceRenderer<>(this::getLocaleDisplayName, String::valueOf));
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

