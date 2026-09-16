@Override
protected void onInitialize() {
    header(new ResourceModel("537"));
    addButton(new BootstrapAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("54"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onError(AjaxRequestTarget target) {
            onErrorAction(target);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            onSubmitAction(target);
        }
    });
    // OK
    addButton(OmModalCloseButton.of());
    add(form.add(feedback.setOutputMarkupId(true), pass.setRequired(false).setLabel(new ResourceModel("110")).setOutputMarkupPlaceholderTag(true).setOutputMarkupId(true)));
    super.onInitialize();
}
// ---- helper method(s) introduced by the refactoring ----
private void onErrorAction(AjaxRequestTarget target) {
    target.add(feedback);
}

private void onSubmitAction(AjaxRequestTarget target) {
    UserForm uf = getUserForm();
    if (uf.isAdminPassRequired() && verifyPassword(uf)) {
        if (action != null) {
            action.accept(target);
        }
        close(target);
    } else {
        form.error(getString("error.bad.password"));
        target.add(feedback);
    }
}

private boolean verifyPassword(UserForm uf) {
    return userDao.verifyPassword(getUserId(), pass.getConvertedInput());
}

