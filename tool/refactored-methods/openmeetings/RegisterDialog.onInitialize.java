@Override
protected void onInitialize() {
    header(new ResourceModel("113"));
    setUseCloseHandler(true);
    addButtons();
    addComponents();
    reset(null);
    super.onInitialize();
}
// ---- helper method(s) introduced by the refactoring ----
private void addButtons() {
    // register
    addButton(new SpinnerAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("121"), form, Buttons.Type.Outline_Primary));
    addButton(OmModalCloseButton.of());
}

private void addComponents() {
    add(form);
    add(new Label("register", getString("121")).setRenderBodyOnly(true), new BookmarkablePageLink<>("link", PrivacyPage.class));
}

