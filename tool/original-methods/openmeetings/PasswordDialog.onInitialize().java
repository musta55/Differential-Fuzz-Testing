@Override
protected void onInitialize() {
    header(new ResourceModel("537"));
    addButton(new BootstrapAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("54"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onError(AjaxRequestTarget target) {
            target.add(feedback);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            final UserForm uf = getUserForm();
            if (uf.isAdminPassRequired()) {
                if (userDao.verifyPassword(getUserId(), pass.getConvertedInput())) {
                    if (action != null) {
                        action.accept(target);
                    }
                    PasswordDialog.this.close(target);
                } else {
                    form.error(getString("error.bad.password"));
                    target.add(feedback);
                }
            } else {
                PasswordDialog.this.close(target);
            }
        }
    });
    // OK
    addButton(OmModalCloseButton.of());
    add(form.add(feedback.setOutputMarkupId(true), pass.setRequired(false).setLabel(new ResourceModel("110")).setOutputMarkupPlaceholderTag(true).setOutputMarkupId(true)));
    super.onInitialize();
}