@Override
protected void onInitialize() {
    header(getTitle());
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
    // add
    addButton(OmModalCloseButton.of());
    RequiredTextField<String> title = new RequiredTextField<>("title", getModel());
    title.setLabel(getLabel());
    form.add(new Label("label", getLabel()), title, feedback.setOutputMarkupId(true), new //FAKE button so "submit-on-enter" works as expected
    AjaxButton(//FAKE button so "submit-on-enter" works as expected
    "submit") {

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
    add(form.setOutputMarkupId(true));
    super.onInitialize();
}