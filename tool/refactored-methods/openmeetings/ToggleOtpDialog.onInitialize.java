@Override
protected void onInitialize() {
    header(new ResourceModel("otp.enable"));
    setUseCloseHandler(true);
    Form<String> form = createForm();
    addFormComponents(form);
    addButtons(form);
    super.onInitialize();
}
// ---- helper method(s) introduced by the refactoring ----
private Form<String> createForm() {
    return new Form<>("form") {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onValidate() {
            User u = ToggleOtpDialog.this.getModelObject();
            String p = current.getConvertedInput();
            if (!Strings.isEmpty(p) && !userDao.verifyPassword(u.getId(), p)) {
                error(getString("231"));
                SignInDialog.penalty();
            }
            super.onValidate();
        }
    };
}

private void addFormComponents(Form<String> form) {
    add(form.add(current.setLabel(new ResourceModel("current.password")).setOutputMarkupId(true)).add(qr.setOutputMarkupId(true)).add(codesArea.setOutputMarkupId(true)).add(feedback.setOutputMarkupId(true)));
}

private void addButtons(Form<String> form) {
    addButton(createSubmitButton(form));
    addButton(OmModalCloseButton.of());
}

private SpinnerAjaxButton createSubmitButton(Form<String> form) {
    return new SpinnerAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("otp.enable"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onError(AjaxRequestTarget target) {
            target.add(feedback);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            User u = ToggleOtpDialog.this.getModelObject();
            u.setOtpSecret(secret);
            u.setOtpRecoveryCodes(String.join(" ", codes));
            EditProfileForm editForm = (EditProfileForm) findParent(EditProfilePanel.class).get("form");
            editForm.updateOtpButton(true, target);
            userDao.update(u, u.getId());
            ToggleOtpDialog.this.close(target);
        }
    };
}

