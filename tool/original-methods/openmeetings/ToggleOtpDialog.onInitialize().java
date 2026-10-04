@Override
protected void onInitialize() {
    header(new ResourceModel("otp.enable"));
    setUseCloseHandler(true);
    final Form<String> form = new Form<>("form") {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onValidate() {
            final User u = ToggleOtpDialog.this.getModelObject();
            String p = current.getConvertedInput();
            if (!Strings.isEmpty(p) && !userDao.verifyPassword(u.getId(), p)) {
                error(getString("231"));
                SignInDialog.penalty();
            }
            super.onValidate();
        }
    };
    add(form.add(current.setLabel(new ResourceModel("current.password")).setOutputMarkupId(true)).add(qr.setOutputMarkupId(true)).add(codesArea.setOutputMarkupId(true)).add(feedback.setOutputMarkupId(true)));
    addButton(new SpinnerAjaxButton(BUTTON_MARKUP_ID, new ResourceModel("otp.enable"), form, Buttons.Type.Outline_Primary) {

        private static final long serialVersionUID = 1L;

        @Override
        protected void onError(AjaxRequestTarget target) {
            target.add(feedback);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            final User u = ToggleOtpDialog.this.getModelObject();
            u.setOtpSecret(secret);
            u.setOtpRecoveryCodes(String.join(" ", codes));
            EditProfileForm editForm = (EditProfileForm) findParent(EditProfilePanel.class).get("form");
            editForm.updateOtpButton(true, target);
            userDao.update(u, u.getId());
            ToggleOtpDialog.this.close(target);
        }
    });
    addButton(OmModalCloseButton.of());
    super.onInitialize();
}