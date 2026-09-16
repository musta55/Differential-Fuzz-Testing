@Override
protected void onInitialize() {
    header(new ResourceModel("113"));
    setUseCloseHandler(true);
    // register
    addButton(new SpinnerAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("121"), form, Buttons.Type.Outline_Primary));
    addButton(OmModalCloseButton.of());
    add(form);
    add(new Label("register", getString("121")).setRenderBodyOnly(true), new BookmarkablePageLink<>("link", PrivacyPage.class));
    reset(null);
    super.onInitialize();
}