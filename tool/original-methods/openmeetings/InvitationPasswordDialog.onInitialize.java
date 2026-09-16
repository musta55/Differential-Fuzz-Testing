@Override
protected void onInitialize() {
    header(new ResourceModel("230"));
    setCloseOnEscapeKey(false);
    setBackdrop(Backdrop.STATIC);
    password.add(new IValidator<String>() {

        private static final long serialVersionUID = 1L;

        @Override
        public void validate(IValidatable<String> validatable) {
            if (!CryptProvider.get().verify(validatable.getValue(), WebSession.get().getInvitation().getPassword())) {
                validatable.error(new ValidationError(getString("error.bad.password")));
            }
        }
    });
    add(form.add(password, feedback.setOutputMarkupId(true)));
    AjaxButton ab = new //FAKE button so "submit-on-enter" works as expected
    AjaxButton(//FAKE button so "submit-on-enter" works as expected
    "submit", //FAKE button so "submit-on-enter" works as expected
    form) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            InvitationPasswordDialog.this.onSubmit(target);
        }

        @Override
        protected void onError(AjaxRequestTarget target) {
            InvitationPasswordDialog.this.onError(target);
        }
    };
    form.add(ab);
    form.setDefaultButton(ab);
    password.setLabel(new ResourceModel("110"));
    addButton(new BootstrapAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("537"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onError(AjaxRequestTarget target) {
            InvitationPasswordDialog.this.onError(target);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            InvitationPasswordDialog.this.onSubmit(target);
        }
    });
    //check
    super.onInitialize();
    Invitation i = WebSession.get().getInvitation();
    show(i != null && i.isPasswordProtected());
}