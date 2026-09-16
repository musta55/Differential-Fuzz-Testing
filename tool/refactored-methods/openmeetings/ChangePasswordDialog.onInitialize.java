@Override
protected void onInitialize() {
    header(new ResourceModel("327"));
    addButton(createSubmitButton());
    addButton(OmModalCloseButton.of());
    StrongPasswordValidator passValidator = new StrongPasswordValidator(userDao.get(getUserId()));
    add(form.add(current.setLabel(new ResourceModel("current.password")).setRequired(true).setOutputMarkupId(true), pass.setLabel(new ResourceModel("328")).add(passValidator).setOutputMarkupId(true), pass2.setLabel(new ResourceModel("116")).setOutputMarkupId(true), feedback.setOutputMarkupId(true)));
    super.onInitialize();
}
// ---- helper method(s) introduced by the refactoring ----
private SpinnerAjaxButton createSubmitButton() {
    return new SpinnerAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("327"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onError(AjaxRequestTarget target) {
            handleError(target);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            handleSubmit(target);
        }
    };
}

private void validateCurrentPassword() {
    String p = current.getConvertedInput();
    if (!Strings.isEmpty(p) && !userDao.verifyPassword(getUserId(), p)) {
        error(getString("231"));
        SignInDialog.penalty();
    }
}

private void validateNewPasswordsMatch() {
    String p1 = pass.getConvertedInput();
    if (!Strings.isEmpty(p1) && !p1.equals(pass2.getConvertedInput())) {
        error(getString("232"));
    }
}

private void handleError(AjaxRequestTarget target) {
    target.add(feedback);
}

private void handleSubmit(AjaxRequestTarget target) {
    try {
        userDao.update(userDao.get(getUserId()), pass.getModelObject(), getUserId());
        close(target);
    } catch (Exception e) {
        error(e.getMessage());
        target.add(feedback);
    }
}

