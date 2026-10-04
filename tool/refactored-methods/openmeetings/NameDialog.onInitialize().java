@Override
protected void onInitialize() {
    header(getTitle());
    addButtons();
    addFormComponents();
    add(form.setOutputMarkupId(true));
    super.onInitialize();
}
// ---- helper method(s) introduced by the refactoring ----
private void addButtons() {
    addButton(new BootstrapAjaxButton(BUTTON_MARKUP_ID, getAddBtnLabel(), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            NameDialog.this.onSubmit(target);
        }

        @Override
        protected void onError(AjaxRequestTarget target) {
            NameDialog.this.onError(target);
        }
    });
    addButton(OmModalCloseButton.of());
    addFakeSubmitButton();
}

private void addFakeSubmitButton() {
    form.add(new AjaxButton("submit") {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            NameDialog.this.onSubmit(target);
        }

        @Override
        protected void onError(AjaxRequestTarget target) {
            NameDialog.this.onError(target);
        }
    });
}

private void addFormComponents() {
    RequiredTextField<String> title = new RequiredTextField<>("title", getModel());
    title.setLabel(getLabel());
    form.add(new Label("label", getLabel()), title, feedback.setOutputMarkupId(true));
}

